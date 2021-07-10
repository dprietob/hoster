package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class AboutDialog extends JDialog
{
    private JFrame parent;
    private JPanel aboutPane;
    private JButton close;
    private JButton reportBug;
    private JEditorPane hosterIsASimpleEditorPane;

    public AboutDialog(JFrame p)
    {
        setContentPane(aboutPane);
        getRootPane().setDefaultButton(close);

        parent = p;
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
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void onClose()
    {
        dispose();
    }
}
