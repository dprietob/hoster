package com.hoster.gui;

import com.hoster.data.Host;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HostDialog extends JDialog
{
    private Host host;
    private int hostPosition;
    private boolean isNew;
    protected HostListener hostListener;

    private JFrame parent;
    private JPanel addHostPane;
    protected JTextField ip;
    protected JTextField domain;
    protected JCheckBox active;
    private JButton cancel;
    private JButton accept;

    public HostDialog(JFrame p, Host h, int pos)
    {
        parent = p;
        host = h;
        hostPosition = pos;

        setContentPane(addHostPane);
        getRootPane().setDefaultButton(accept);
        setHostConfig();

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
        if (isNew) {
            setIconImage(new ImageIcon(getClass().getResource("icons/add.png")).getImage());
            setTitle("Add new host");
        } else {
            setIconImage(new ImageIcon(getClass().getResource("icons/edit.png")).getImage());
            setTitle("Edit host");
        }

        pack();
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void setHostConfig()
    {
        if (host == null) {
            host = new Host();
            isNew = true;
        } else {
            active.setSelected(host.isActive());
            ip.setText(host.getIp());
            domain.setText(host.getServerName());
            isNew = false;
        }
    }

    protected void onAccept()
    {
        if (fieldsFilled()) {
            host.setActive(active.isSelected());
            host.setIp(ip.getText());
            host.setServerName(domain.getText());

            if (isNew) {
                hostListener.onHostAdded(host);
            } else {
                hostListener.onHostEdited(host, hostPosition);
            }

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
        return !ip.getText().isEmpty()
            && !domain.getText().isEmpty();
    }
}
