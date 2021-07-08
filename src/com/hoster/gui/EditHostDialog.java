package com.hoster.gui;

import com.hoster.Host;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditHostDialog extends AddHostDialog
{
    private Host host;
    private int hostPosition;

    public EditHostDialog(JFrame p, Host h, int pos)
    {
        super(p);
        host = h;
        hostPosition = pos;

        active.setSelected(h.isActive());
        ip.setText(h.getIp());
        domain.setText(h.getDomain());
    }

    public void build()
    {
        super.build();
        setIconImage(new ImageIcon(getClass().getResource("icons/edit.png")).getImage());
        setTitle("Edit host");
    }

    protected void onAccept()
    {
        if (fieldsFilled()) {
            host.setActive(active.isSelected());
            host.setIp(ip.getText());
            host.setDomain(domain.getText());

            hostListener.onHostEdited(host, hostPosition);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "IP and domain fields can not be empty.",
                "Empty fields",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
