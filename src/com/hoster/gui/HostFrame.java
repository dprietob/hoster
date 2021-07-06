package com.hoster.gui;

import com.hoster.files.Hosts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

public class HostFrame extends JFrame
{
    private final String APP_NAME = "Hoster";
    private final String APP_VERSION = "0.1.0";

    private Map<String, String> hosts;
    private Map<String, String> properties;
    private JPanel domainPane;
    private JButton addDomain;
    private JButton deleteDomain;
    private JButton virtualHost;
    private JButton mainConfig;
    private JButton about;
    private JTable hostsTable;

    public HostFrame(Map<String, String> hostsConfig, Map<String, String> propertiesConfig)
    {
        hosts = hostsConfig;
        properties = propertiesConfig;

        setContentPane(domainPane);
        updateHostsTable();

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
    }

    public void build()
    {
        pack();
        setTitle(APP_NAME + " " + APP_VERSION);
        setIconImage(new ImageIcon(getClass().getResource("icons/icon.png")).getImage());
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void updateHostsTable()
    {
        DefaultTableModel tableModel = new DefaultTableModel();
        hostsTable.setModel(tableModel);

        tableModel.addColumn("IP");
        tableModel.addColumn("Domain");

        for (Map.Entry<String, String> entry : hosts.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void onAddDomain()
    {

    }

    private void onDeleteDomain()
    {
        int row = hostsTable.getSelectedRow();
        if (row > -1) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this host? This action can not be undone.",
                "Delete host",
                JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                String ip = hostsTable.getValueAt(row, 0).toString();
                hosts.remove(ip);
                if (Hosts.save(properties.get("hosts_file"), hosts, APP_NAME, APP_VERSION)) {
                    updateHostsTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "There was an error trying to update host file. Make sure the application has the necessary privileges.",
                        "Host update error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
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
