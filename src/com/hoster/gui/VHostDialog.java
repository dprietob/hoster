package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class VHostDialog extends JDialog
{
    private JFrame parent;
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

    public VHostDialog(JFrame p)
    {
        setContentPane(vhostPane);
        getRootPane().setDefaultButton(accept);

        parent = p;
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
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    private void onAccept()
    {
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }
}
