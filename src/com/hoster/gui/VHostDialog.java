package com.hoster.gui;

import com.hoster.data.Host;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class VHostDialog extends JDialog
{
    private Host host;
    private int hostPosition;
    private HostListener hostListener;

    private JFrame parent;
    private JPanel vhostPane;
    private JTextField serverAdmin;
    private JTextField serverName;
    private JTextField documentRoot;
    private JButton findDocumentRootPath;
    private JTextField serverAlias;
    private JTextField port;
    private JTextField errorLog;
    private JButton findErrorLogPath;
    private JTextField customLog;
    private JButton findCustomLogPath;
    private JButton accept;
    private JButton cancel;
    private JTextField require;
    private JTextField allowOverride;

    public VHostDialog(JFrame p, Host h, int pos)
    {
        parent = p;
        host = h;
        hostPosition = pos;

        setContentPane(vhostPane);
        getRootPane().setDefaultButton(accept);
        setVHostConfig();

        findDocumentRootPath.addActionListener(e -> onFind(documentRoot, JFileChooser.DIRECTORIES_ONLY));
        findErrorLogPath.addActionListener(e -> onFind(errorLog, JFileChooser.FILES_ONLY));
        findCustomLogPath.addActionListener(e -> onFind(customLog, JFileChooser.FILES_ONLY));
        accept.addActionListener(e -> onAccept());
        cancel.addActionListener(e -> onCancel());

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

        // Call onCancel() on ESCAPE
        vhostPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addHostListener(HostListener listener)
    {
        hostListener = listener;
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/vhost.png")).getImage());
        setTitle("Virtual host configuration");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void setVHostConfig()
    {
        serverAdmin.setText(host.getServerAdmin());
        serverName.setText(host.getServerName());
        documentRoot.setText(host.getDocumentRoot());
        serverAlias.setText(host.getServerAlias());
        port.setText(host.getPort());
        errorLog.setText(host.getErrorLog());
        customLog.setText(host.getCustomLog());
        require.setText(host.getDirectory().getRequire());
        allowOverride.setText(host.getDirectory().getAllowOverride());
    }

    private void onFind(JTextField field, int mode)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setCurrentDirectory(new File(field.getText()));
        int selection = fileChooser.showOpenDialog(field);

        if (selection == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    protected void onAccept()
    {
        if (fieldsFilled()) {
            host.setServerAdmin(serverAdmin.getText());
            host.setServerName(serverName.getText());
            host.setDocumentRoot(documentRoot.getText());
            host.setServerAlias(serverAlias.getText());
            host.setPort(port.getText());
            host.setErrorLog(errorLog.getText());
            host.setCustomLog(customLog.getText());
            host.getDirectory().setRequire(require.getText());
            host.getDirectory().setAllowOverride(allowOverride.getText());

            hostListener.onHostEdited(host, hostPosition);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Server name and document root fields can not be empty.",
                "Empty fields",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void onCancel()
    {
        dispose();
    }

    protected boolean fieldsFilled()
    {
        return !serverName.getText().isEmpty()
            && !documentRoot.getText().isEmpty();
    }
}
