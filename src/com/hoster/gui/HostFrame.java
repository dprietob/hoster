package com.hoster.gui;

import com.hoster.Host;
import com.hoster.files.HostsFile;
import com.hoster.gui.listeners.ConfigListener;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

public class HostFrame extends JFrame implements HostListener, ConfigListener
{
    private final String APP_NAME = "Hoster";
    private final String APP_VERSION = "0.1.0";

    private Map<String, Host> hostsMap;
    private Map<String, String> propertiesMap;
    private JPanel domainPane;
    private JButton addDomain;
    private JButton deleteDomain;
    private JButton virtualHost;
    private JButton mainConfig;
    private JButton about;
    private JTable hostsTable;

    public HostFrame(Map<String, Host> hostsConfig, Map<String, String> propertiesConfig)
    {
        hostsMap = hostsConfig;
        propertiesMap = propertiesConfig;

        setContentPane(domainPane);
        updateHostsTable();

        addDomain.addActionListener(e -> onAddHostDialog());
        deleteDomain.addActionListener(e -> onHostDeleted());
        virtualHost.addActionListener(e -> onVirtualHostDialog());
        mainConfig.addActionListener(e -> onMainConfigDialog());
        about.addActionListener(e -> onAboutDialog());

        // Call onAddHostDialog() on CTRL+N
        domainPane.registerKeyboardAction(e -> onAddHostDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onHostDeleted() on DEL
        domainPane.registerKeyboardAction(e -> onHostDeleted(), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onVirtualHostDialog() on CTRL+H
        domainPane.registerKeyboardAction(e -> onVirtualHostDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onMainConfigDialog() on CTRL+Q
        domainPane.registerKeyboardAction(e -> onMainConfigDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call )() on F1
        domainPane.registerKeyboardAction(e -> onAboutDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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
        tableModel.addColumn("IP");
        tableModel.addColumn("Domain");

        hostsTable.setModel(tableModel);
        hostsTable.getTableHeader().setReorderingAllowed(false);

        Host host;

        for (Map.Entry<String, Host> entry : hostsMap.entrySet()) {
            host = entry.getValue();
            tableModel.addRow(new Object[]{host.getIp(), host.getDomain()});
        }
    }

    private void onAddHostDialog()
    {
        AddHostDialog dialog = new AddHostDialog(this);
        dialog.addHostListener(this);
        dialog.build();
    }

    private void onVirtualHostDialog()
    {
        int row = hostsTable.getSelectedRow();
        if (row > -1) {
            VHostDialog dialog = new VHostDialog(this);
            dialog.build();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "You must select a domain to configure it's virtual host.",
                "Virtual host error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onMainConfigDialog()
    {
        ConfigDialog dialog = new ConfigDialog(this, propertiesMap);
        dialog.addConfigListener(this);
        dialog.build();
    }

    private void onAboutDialog()
    {
        AboutDialog dialog = new AboutDialog(this);
        dialog.build();
    }

    @Override
    public void onHostAdded(Host host)
    {
        hostsMap.put(host.getDomain(), host);
        if (HostsFile.save(propertiesMap.get("hosts_file"), hostsMap, APP_NAME, APP_VERSION)) {
            updateHostsTable();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "There was an error trying to update host file. Make sure the application has the necessary privileges.",
                "Host file update error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onHostDeleted()
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
                hostsMap.remove(ip);
                if (HostsFile.save(propertiesMap.get("hosts_file"), hostsMap, APP_NAME, APP_VERSION)) {
                    updateHostsTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "There was an error trying to update host file. Make sure the application has the necessary privileges.",
                        "Host file update error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "You must select a host to delete it.",
                "Host delete error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onVirtualHostUpdated(Host host)
    {
        hostsMap.put(host.getDomain(), host);
    }

    @Override
    public void onConfigUpdate()
    {
        hostsMap = HostsFile.load(propertiesMap.get("hosts_file"));
        updateHostsTable();
    }
}
