package com.hoster.gui;

import javax.swing.*;
import java.awt.event.*;

public class DomainFrame extends JFrame
{
    private JPanel domainPane;
    private JButton addDomain;
    private JButton deleteDomain;
    private JButton virtualHost;
    private JButton mainConfig;
    private JButton about;
    private JTable domains;

    public DomainFrame()
    {
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

        createTable();
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

    private void createTable()
    {
        String[] columnNames = {"IP", "Domain"};
        Object[][] data = {{"Kathy", "Smith"}, {"John", "Doe"}};
        domains = new JTable(data, columnNames);
    }

    private void onAddDomain()
    {

    }

    private void onDeleteDomain()
    {
        dispose();
    }

    private void onVirtualHostConfigure()
    {
        System.out.println(this);
        VHostDialog dialog = new VHostDialog(this);
        dialog.build();
    }

    private void onMainConfig()
    {
        ConfigDialog dialog = new ConfigDialog(this);
        dialog.build();
    }

    private void onAbout()
    {
        AboutDialog dialog = new AboutDialog(this);
        dialog.build();
    }
}
