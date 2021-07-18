package com.hoster.gui;

import com.hoster.data.HList;
import com.hoster.data.Host;
import com.hoster.data.HostList;
import com.hoster.data.HostStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HostsTableRenderer extends DefaultTableCellRenderer
{
    private final Color bgStandard = UIManager.getColor("Table.background");
    private final Color fgStandard = UIManager.getColor("Table.foreground");
    private final Color bgSelected = UIManager.getColor("Table.selectionBackground");
    private final Color fgSelected = UIManager.getColor("Table.selectionForeground");
    private final Color bgFocus = UIManager.getColor("Table.focusCellBackground");
    private final Color fgFocus = UIManager.getColor("Table.focusCellForeground");
    private final Color bgInactive = UIManager.getColor("Table.background").darker();
    private final Color fgInactive = UIManager.getColor("Table.background").brighter();
    private final Color bgNoDomain = new Color(255, 212, 212);
    private final Color fgNoDomain = bgNoDomain.darker();
    private final Color bgNoVhost = new Color(255, 255, 170);
    private final Color fgNoVhost = bgNoVhost.darker();

    private HList hostList;

    public HostsTableRenderer(HList hl)
    {
        hostList = hl;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Host h = hostList.get(row);

        Color background;
        Color foreground;
        String details;
        ImageIcon icon;

        if (h.getStatus() == HostStatus.NO_DOMAIN) {
            background = bgNoDomain;
            foreground = fgNoDomain;
            details = "No IP or domain defined";
            icon = new ImageIcon(getClass().getResource("icons/no_domain.png"));

        } else if (h.getStatus() == HostStatus.NO_VHOST) {
            background = bgNoVhost;
            foreground = fgNoVhost;
            details = "No virtual host defined";
            icon = new ImageIcon(getClass().getResource("icons/no_vhost.png"));

        } else if (!h.isActive()) {
            background = bgInactive;
            foreground = fgInactive;
            details = "Domain inactive";
            icon = new ImageIcon(getClass().getResource("icons/inactive.png"));

        } else {
            background = bgStandard;
            foreground = fgStandard;
            details = "";
            icon = new ImageIcon(getClass().getResource("icons/all_ok.png"));
        }

        if (isSelected) {
            background = bgSelected;
            foreground = fgSelected;

        } else if (hasFocus) {
            background = bgFocus;
            foreground = fgFocus;
        }

        c.setBackground(background);
        c.setForeground(foreground);

        // Status column
        if (column == 0) {
            ((JLabel) c).setText("");
            ((JLabel) c).setIcon(icon);

        // Details column
        } else if (column == 3) {
            ((JLabel) c).setText(details);

        } else {
            ((JLabel) c).setIcon(null);
        }

        return c;
    }
}
