package com.hoster.gui;

import com.hoster.data.Host;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class HostsTableRenderer extends DefaultTableCellRenderer
{
    private List<Host> hostList;

    public HostsTableRenderer(List<Host> hl)
    {
        hostList = hl;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Host h = hostList.get(row);
        Color background = UIManager.getColor("Table.background");
        Color foreground = UIManager.getColor("Table.foreground");

        if (isSelected) {
            background = UIManager.getColor("Table.selectionBackground");
            foreground = UIManager.getColor("Table.selectionForeground");
        } else if (hasFocus) {
            background = UIManager.getColor("Table.focusCellBackground");
            foreground = UIManager.getColor("Table.focusCellForeground");
        } else if (!h.isActive()) {
            background = UIManager.getColor("Table.background").darker();
            foreground = UIManager.getColor("Table.background").brighter();
        }

        c.setBackground(background);
        c.setForeground(foreground);

        return c;
    }
}
