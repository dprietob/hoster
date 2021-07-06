package com.hoster.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

public class DomainFrame extends JFrame
{
    private Map<String,String> properties;
    private JPanel domainPane;
    private JButton addDomain;
    private JButton deleteDomain;
    private JButton virtualHost;
    private JButton mainConfig;
    private JButton about;
    private JTable domains;

    public DomainFrame(Map<String,String> config)
    {
        properties = config;

        setContentPane(domainPane);

        addDomain.addActionListener(e -> onAddDomain());
        deleteDomain.addActionListener(e -> onDeleteDomain());
        virtualHost.addActionListener(e -> onVirtualHostConfigure());
        mainConfig.addActionListener(e -> onMainConfig());
        about.addActionListener(e -> onAbout());

        // Call onAddDomain() on CTRL+N
        domainPane.registerKeyboardAction(e -> onAddDomain(), KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onDeleteDomain() on DEL
        domainPane.registerKeyboardAction(e -> onDeleteDomain(), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onVirtualHostConfigure() on CTRL+H
        domainPane.registerKeyboardAction(e -> onVirtualHostConfigure(), KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onVirtualHostConfigure() on CTRL+Q
        domainPane.registerKeyboardAction(e -> onMainConfig(), KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onAbout() on F1
        domainPane.registerKeyboardAction(e -> onAddDomain(), KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setDomainsToTable();
    }

    public void build()
    {
        pack();
        setTitle("Hoster 0.1.0");
        setIconImage(new ImageIcon(getClass().getResource("icons/icon.png")).getImage());
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setDomainsToTable()
    {
        DefaultTableModel tableModel= new DefaultTableModel();
        domains.setModel(tableModel);

        tableModel.addColumn("IP");
        tableModel.addColumn("Domain");

        Object[] columna= new Object[3];

//        int objGuardados= estDAO.extraerTodos().size();
//
//        for (int i = 0; i < objGuardados; i++) {
//            columna[0]= estDAO.extraerTodos().get(i).getNombre();
//            columna[1]= estDAO.extraerTodos().get(i).getMatricula();
//            columna[2]= estDAO.extraerTodos().get(i).getNota();
//
//            modeloTabla.addRow(columna);
//        }
    }

    private void onAddDomain()
    {

    }

    private void onDeleteDomain()
    {

    }

    private void onVirtualHostConfigure()
    {
        VHostDialog dialog = new VHostDialog(this);
        dialog.build();
    }

    private void onMainConfig()
    {
        ConfigDialog dialog = new ConfigDialog(this, properties);
        dialog.build();
    }

    private void onAbout()
    {
        AboutDialog dialog = new AboutDialog(this);
        dialog.build();
    }
}
