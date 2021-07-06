package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class ConfigDialog extends JDialog
{
    private JFrame parent;
    private JTextField domainsFile;
    private JTextField vhostsFile;
    private JButton findDomainsFile;
    private JButton findVhostFile;
    private JButton cancel;
    private JButton accept;
    private JPanel configPane;
    private JTextField require;
    private JTextField allowOverride;
    private JTextField directoryPath;
    private JButton findDirectoryPath;
    private JComboBox theme;

    public ConfigDialog(JFrame p)
    {
        setContentPane(configPane);
        setModal(true);
        getRootPane().setDefaultButton(accept);

        parent = p;
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
        configPane.registerKeyboardAction(new ActionListener()
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
        setIconImage(new ImageIcon(getClass().getResource("icons/settings.png")).getImage());
        setTitle("Main configuration");
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
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
