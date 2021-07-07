package com.hoster.gui;

import com.hoster.Host;
import com.hoster.files.HostsFile;
import com.hoster.files.PropertiesFile;
import com.hoster.files.VHostsFile;
import com.hoster.gui.listeners.PropertiesListener;
import com.hoster.gui.listeners.HostListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class HostFrame extends JFrame implements HostListener, PropertiesListener
{
    private final String APP_NAME = "Hoster";
    private final String APP_VERSION = "0.1.0";

    private List<Host> hostsList;
    private Map<String, String> propertiesMap;
    private JPanel domainPane;
    private JButton addDomain;
    private JButton deleteDomain;
    private JButton virtualHost;
    private JButton mainConfig;
    private JButton about;
    private JTable hostsTable;

    public HostFrame(List<Host> hostsConfig, Map<String, String> propertiesConfig)
    {
        hostsList = hostsConfig;
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

        for (Host host : hostsList) {
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
            VHostDialog dialog = new VHostDialog(this, hostsList.get(row), row);
            dialog.addHostListener(this);
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
        PropertiesDialog dialog = new PropertiesDialog(this, propertiesMap);
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
        hostsList.add(host);
        if (HostsFile.save(propertiesMap.get("hosts_file"), hostsList, APP_NAME, APP_VERSION)) {
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
                "Are you sure you want to delete this host? This action will remove it's virtual host configuration and can not be undone.",
                "Delete host",
                JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                hostsList.remove(row);
                if (HostsFile.save(propertiesMap.get("hosts_file"), hostsList, APP_NAME, APP_VERSION)) {
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
    public void onVirtualHostUpdated(Host host, int row)
    {
        hostsList.remove(row);
        hostsList.add(row, host);

        if (!VHostsFile.save(propertiesMap.get("vhosts_file"), hostsList, APP_NAME, APP_VERSION)) {
            JOptionPane.showMessageDialog(
                this,
                "There was an error trying to update virtual-host file. Make sure the application has the necessary privileges.",
                "Host file update error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onPropertiesUpdate()
    {
        if (!PropertiesFile.save(propertiesMap)) {
            JOptionPane.showMessageDialog(
                this,
                "An error occurred while trying to save the properties file. Please make sure the application has the necessary privileges.",
                "Properties file error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
