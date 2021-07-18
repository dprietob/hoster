package com.hoster.gui;

import com.hoster.data.HList;
import com.hoster.data.Host;
import com.hoster.data.Properties;
import com.hoster.data.Server;
import com.hoster.gui.listeners.ConsoleListener;
import com.hoster.gui.listeners.HostListener;
import com.hoster.gui.listeners.PropertiesListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame implements HostListener, PropertiesListener, ConsoleListener
{
    private final String APP_NAME = "Hoster";
    private final String APP_VERSION = "0.1.0";

    private final Color ERROR_COLOR = new Color(220, 0, 0);
    private final Color INFO_COLOR = UIManager.getColor("TextArea.foreground");

    private final int SERVER_ACTIVED = 1;
    private final int SERVER_STOPPED = 2;
    private final int SERVER_RESTARTING = 3;


    private Properties properties;
    private HList hostsList;
    private Server server;
    private StyledDocument styledDocument;
    private Style consoleStyle;

    private JPanel domainPane;
    private JButton addHostBtn;
    private JButton editHostBtn;
    private JButton deleteHostBtn;
    private JButton virtualHostBtn;
    private JButton phpBtn;
    private JButton mainConfigBtn;
    private JButton aboutBtn;
    private JTable hostsTable;
    private JPanel consolePane;
    private JTextPane consoleLog;
    private JButton restartServerBtn;
    private JLabel serverStatus;
    private JButton serverStatsBtn;

    public MainFrame(Properties prop, HList hl)
    {
        properties = prop;
        hostsList = hl;

        server = new Server();
        server.addConsoleListener(this);

        styledDocument = consoleLog.getStyledDocument();
        consoleStyle = consoleLog.addStyle("Console Style", null);

        setContentPane(domainPane);
        updateHostsTable();
        updateConsolePane();
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

        addWindowFocusListener(new WindowFocusListener()
        {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent)
            {
                updateServerStatus(false);
            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent)
            {

            }
        });
    }

    public void build()
    {
        String[] sizes = {"16", "24", "32", "64", "128", "256", "512"};
        List<Image> icons = new ArrayList<>();

        for (String size : sizes) {
            icons.add(new ImageIcon(getClass().getResource("icons/logo/logo_" + size + ".png")).getImage());
        }

        pack();
        setTitle(APP_NAME + " " + APP_VERSION);
        setIconImages(icons);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected void updateHostsTable()
    {
        HostsTableModel tableModel = new HostsTableModel();
        tableModel.addColumn("Status");
        tableModel.addColumn("IP");
        tableModel.addColumn("Domain");
        tableModel.addColumn("Details");

        hostsTable.setModel(tableModel);
        hostsTable.setDefaultRenderer(Object.class, new HostsTableRenderer(hostsList));
        hostsTable.getTableHeader().setReorderingAllowed(false);
        hostsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        hostsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        hostsTable.getColumnModel().getColumn(1).setMaxWidth(200);

        for (Host host : hostsList) {
            tableModel.addRow(new Object[]{host.getStatus(), host.getIp(), host.getServerName()});
        }
    }

    protected void updateConsolePane()
    {
        consolePane.setVisible(properties.getBoolean("console_log"));
    }

    protected void updateServerStatus()
    {
        updateServerStatus(true);
    }

    protected void updateServerStatus(boolean notifyConsole)
    {
        if (server.isActive()) {
            setServerStatus(SERVER_ACTIVED);

            if (notifyConsole) {
                onConsoleInfo("Server active");
            }
        } else {
            setServerStatus(SERVER_STOPPED);

            if (notifyConsole) {
                onConsoleError("Server stopped");
            }
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

    protected void saveHostFile()
    {
        boolean fileSaved = properties.getHostsFile().save(
            properties.getString("hosts_file"),
            hostsList,
            APP_NAME,
            APP_VERSION
        );

        if (!fileSaved) {
            JOptionPane.showMessageDialog(
                this,
                "There was an error trying to update host file. Make sure the application has the necessary privileges.",
                "Host file update error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    protected void saveVHostFile()
    {
        boolean fileSaved = properties.getVHostsFile().save(
            properties.getString("vhosts_file"),
            properties.getMainDirectory(),
            hostsList,
            APP_NAME,
            APP_VERSION
        );

        if (!fileSaved) {
            JOptionPane.showMessageDialog(
                this,
                "There was an error trying to update virtual-host file. Make sure the application has the necessary privileges.",
                "Virtual-Host file update error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    protected void savePropertiesFile()
    {
        boolean fileSaved = properties.getPropertiesFile().save(properties);

        if (!fileSaved) {
            JOptionPane.showMessageDialog(
                this,
                "An error occurred while trying to save the properties file. Please make sure the application has the necessary privileges.",
                "Properties file error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    protected String getCurrentTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

    protected void onAddHostDialog()
    {
        HostDialog dialog = new HostDialog(this, null, 0);
        dialog.addHostListener(this);
        dialog.build();
    }

    protected void onEditHostDialog()
    {
        int row = hostsTable.getSelectedRow();
        if (row > -1) {
            HostDialog dialog = new HostDialog(this, hostsList.get(row), row);
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
        PropertiesDialog dialog = new PropertiesDialog(this, properties, server.getOS());
        dialog.addConfigListener(this);
        dialog.build();
    }

    protected void onAboutDialog()
    {
        AboutDialog dialog = new AboutDialog(this);
        dialog.addConsoleListener(this);
        dialog.build();
    }

    protected void onRestartServer()
    {
        onRestartServer(false);
    }

    protected void onRestartServer(boolean force)
    {
        if (force || properties.getBoolean("restart_server")) {
            onConsoleInfo("Restarting server...");
            setServerStatus(SERVER_RESTARTING);
            server.restart(properties.getString("apache_path"));
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
        saveHostFile();
        updateHostsTable();
        onRestartServer();
    }

    @Override
    public void onHostEdited(Host host, int row)
    {
        hostsList.remove(row);
        hostsList.add(row, host);
        saveHostFile();
        saveVHostFile();
        updateHostsTable();
        onRestartServer();
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
                saveHostFile();
                saveVHostFile();
                updateHostsTable();
                onRestartServer();
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
    public void onPropertiesUpdate(Map<String, Object> propertiesMap)
    {
        properties.setPropertiesMap(propertiesMap);
        savePropertiesFile();
        updateConsolePane();
        onRestartServer();
    }

    @Override
    public void onConsoleInfo(String info)
    {
        StyleConstants.setForeground(consoleStyle, INFO_COLOR);

        try {
            styledDocument.insertString(styledDocument.getLength(), getCurrentTime() + " - " + info + "\n", consoleStyle);
        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Console error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onConsoleError(String error)
    {
        StyleConstants.setForeground(consoleStyle, ERROR_COLOR);

        try {
            styledDocument.insertString(styledDocument.getLength(), getCurrentTime() + " - " + error + "\n", consoleStyle);
        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Console error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
