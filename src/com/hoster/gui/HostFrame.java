package com.hoster.gui;

import com.hoster.data.Host;
import com.hoster.data.Properties;
import com.hoster.data.Server;
import com.hoster.files.HostsFile;
import com.hoster.files.PropertiesFile;
import com.hoster.files.VHostsFile;
import com.hoster.gui.listeners.HostListener;
import com.hoster.gui.listeners.PropertiesListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.List;
import java.util.Map;

public class HostFrame extends JFrame implements HostListener, PropertiesListener
{
    private final String APP_NAME = "Hoster";
    private final String APP_VERSION = "0.1.0";

    private final int SERVER_ACTIVED = 1;
    private final int SERVER_STOPPED = 2;
    private final int SERVER_RESTARTING = 3;

    private List<Host> hostsList;
    private Properties properties;
    private JPanel domainPane;
    private JButton addHostBtn;
    private JButton editHostBtn;
    private JButton deleteHostBtn;
    private JButton virtualHostBtn;
    private JButton phpBtn;
    private JButton mainConfigBtn;
    private JButton aboutBtn;
    private JTable hostsTable;
    private JButton restartServerBtn;
    private JLabel serverStatus;
    private JButton serverStatsBtn;

    public HostFrame(List<Host> hl, Properties prop)
    {
        hostsList = hl;
        properties = prop;

        setContentPane(domainPane);
        updateHostsTable();
        updateServerStatus();

        // Call onAddHostDialog() on CTRL+N
        domainPane.registerKeyboardAction(e -> onAddHostDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onEditHostDialog() on ENTER
        domainPane.registerKeyboardAction(e -> onEditHostDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onHostDeleted() on DEL
        domainPane.registerKeyboardAction(e -> onHostDeleted(), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onVirtualHostDialog() on CTRL+H
        domainPane.registerKeyboardAction(e -> onVirtualHostDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onPhpConfigDialog() on CTRL+H
        domainPane.registerKeyboardAction(e -> onPhpConfigDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onMainConfigDialog() on CTRL+Q
        domainPane.registerKeyboardAction(e -> onMainConfigDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onAboutDialog() on F1
        domainPane.registerKeyboardAction(e -> onAboutDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onMainConfigDialog() on CTRL+R
        domainPane.registerKeyboardAction(e -> onRestartServer(true), KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Call onServerStatsDialog() on CTRL+U
        domainPane.registerKeyboardAction(e -> onServerStats(), KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        addHostBtn.addActionListener(e -> onAddHostDialog());
        editHostBtn.addActionListener(e -> onEditHostDialog());
        deleteHostBtn.addActionListener(e -> onHostDeleted());
        virtualHostBtn.addActionListener(e -> onVirtualHostDialog());
        phpBtn.addActionListener(e -> onPhpConfigDialog());
        mainConfigBtn.addActionListener(e -> onMainConfigDialog());
        aboutBtn.addActionListener(e -> onAboutDialog());
        restartServerBtn.addActionListener(e -> onRestartServer(true));
        serverStatsBtn.addActionListener(e -> onServerStats());

//        addWindowFocusListener(new WindowFocusListener()
//        {
//            @Override
//            public void windowGainedFocus(WindowEvent windowEvent)
//            {
//                updateServerStatus();
//            }
//
//            @Override
//            public void windowLostFocus(WindowEvent windowEvent)
//            {
//
//            }
//        });
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

    protected void updateHostsTable()
    {
        HostsTableModel tableModel = new HostsTableModel();
        tableModel.addColumn("IP");
        tableModel.addColumn("Domain");

        hostsTable.setModel(tableModel);
        hostsTable.setDefaultRenderer(Object.class, new HostsTableRenderer(hostsList));
        hostsTable.getTableHeader().setReorderingAllowed(false);
        hostsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (Host host : hostsList) {
            tableModel.addRow(new Object[]{host.getIp(), host.getDomain()});
        }
    }

    protected void updateServerStatus()
    {
        if (Server.isActive()) {
            setServerStatus(SERVER_ACTIVED);
        } else {
            setServerStatus(SERVER_STOPPED);
        }
    }

    protected void setServerStatus(int status)
    {
        String text = "unknoww";
        Color color = UIManager.getColor("Label.foreground");

        switch (status) {
            case SERVER_ACTIVED:
                text = "actived";
                color = Color.GREEN.darker();
                break;
            case SERVER_STOPPED:
                text = "stopped";
                color = Color.RED;
                break;
            case SERVER_RESTARTING:
                text = "restarting...";
                color = Color.ORANGE.darker();
                break;
        }
        serverStatus.setText(text);
        serverStatus.setForeground(color);
    }

    protected void onAddHostDialog()
    {
        AddHostDialog dialog = new AddHostDialog(this);
        dialog.addHostListener(this);
        dialog.build();
    }

    protected void onEditHostDialog()
    {
        int row = hostsTable.getSelectedRow();
        if (row > -1) {
            EditHostDialog dialog = new EditHostDialog(this, hostsList.get(row), row);
            dialog.addHostListener(this);
            dialog.build();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "You must select a domain to edit it.",
                "Edit host error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    protected void onVirtualHostDialog()
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

    protected void onPhpConfigDialog()
    {
        // TODO
    }

    protected void onMainConfigDialog()
    {
        PropertiesDialog dialog = new PropertiesDialog(this, properties);
        dialog.addConfigListener(this);
        dialog.build();
    }

    protected void onAboutDialog()
    {
        AboutDialog dialog = new AboutDialog(this);
        dialog.build();
    }

    protected void onRestartServer()
    {
        onRestartServer(false);
    }

    protected void onRestartServer(boolean force)
    {
        if (force || properties.getBoolean("restart_server")) {
            setServerStatus(SERVER_RESTARTING);
            Server.restart();
            updateServerStatus();
        }
    }

    protected void onServerStats()
    {
        // TODO
    }

    @Override
    public void onHostAdded(Host host)
    {
        hostsList.add(host);

        if (HostsFile.save(properties.getString("hosts_file"), hostsList, APP_NAME, APP_VERSION)) {
            updateHostsTable();
            onRestartServer();
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
    public void onHostEdited(Host host, int row)
    {
        hostsList.remove(row);
        hostsList.add(row, host);

        if (HostsFile.save(properties.getString("hosts_file"), hostsList, APP_NAME, APP_VERSION)) {
            updateHostsTable();
            onRestartServer();
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
                // TODO: debe actualizar VHostFile tambien
                if (HostsFile.save(properties.getString("hosts_file"), hostsList, APP_NAME, APP_VERSION)) {
                    updateHostsTable();
                    onRestartServer();
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

        if (VHostsFile.save(properties.getString("vhosts_file"), hostsList, properties.getMainDirectory(), APP_NAME, APP_VERSION)) {
            onRestartServer();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "There was an error trying to update virtual-host file. Make sure the application has the necessary privileges.",
                "Host file update error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onPropertiesUpdate(Map<String, Object> propertiesMap)
    {
        properties.setPropertiesMap(propertiesMap);

        if (PropertiesFile.save(properties)) {
            onRestartServer();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "An error occurred while trying to save the properties file. Please make sure the application has the necessary privileges.",
                "Properties file error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
