package com.hoster.gui;

import javax.swing.table.DefaultTableModel;

public class HostsTableModel extends DefaultTableModel
{
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
