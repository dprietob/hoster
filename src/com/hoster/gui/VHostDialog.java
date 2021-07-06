package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class VHostDialog extends JDialog
{
    private JPanel vhostPane;
    private JTextField serverAdmin;
    private JTextField serverName;
    private JTextField documentRoot;
    private JTextField serverAlias;
    private JTextField port;
    private JTextField errorLog;
    private JTextField customLog;
    private JButton accept;
    private JButton cancel;
    private JTextField require;
    private JTextField allowOverride;

    public VHostDialog()
    {
        setContentPane(vhostPane);
        setModal(true);
        getRootPane().setDefaultButton(accept);

        accept.addActionListener(e -> onOK());
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
        vhostPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/vhost.png")).getImage());
        setTitle("Virtual host configuration");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void onOK()
    {
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }
}
