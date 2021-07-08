package com.hoster.gui;

import com.hoster.Host;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddHostDialog extends JDialog
{
    protected HostListener hostListener;
    private JFrame parent;
    private JPanel addHostPane;
    protected JTextField ip;
    protected JTextField domain;
    protected JCheckBox active;
    private JButton cancel;
    private JButton accept;

    public AddHostDialog(JFrame p)
    {
        parent = p;

        setContentPane(addHostPane);
        getRootPane().setDefaultButton(accept);

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
        addHostPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addHostListener(HostListener listener)
    {
        hostListener = listener;
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/add.png")).getImage());
        setTitle("Add new host");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void onAccept()
    {
        if (fieldsFilled()) {
            Host host = new Host();
            host.setActive(active.isSelected());
            host.setIp(ip.getText());
            host.setDomain(domain.getText());

            hostListener.onHostAdded(host);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "IP and domain fields can not be empty.",
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
        return !ip.getText().equals("") || !domain.getText().equals("");
    }
}
