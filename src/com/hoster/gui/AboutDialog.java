package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class AboutDialog extends JDialog
{
    private JPanel aboutPane;
    private JButton close;

    public AboutDialog()
    {
        setContentPane(aboutPane);
        setModal(true);
        getRootPane().setDefaultButton(close);

        close.addActionListener(e -> onClose());

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onClose();
            }
        });

        // Call onCancel() on ESCAPE
        aboutPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/help.png")).getImage());
        setTitle("About");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void onClose()
    {
        dispose();
    }
}
