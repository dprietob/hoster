package com.hoster.gui;

import com.hoster.data.OperatingSystem;
import com.hoster.data.Properties;
import com.hoster.gui.listeners.PropertiesListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PropertiesDialog extends JDialog
{
    private Properties properties;
    private PropertiesListener propertiesListener;

    private JFrame parent;
    private JPanel configPane;
    private JComboBox theme;
    private JTextField hostsFile;
    private JButton findHostsFile;
    private JCheckBox consoleLog;
    private JTextField vhostsFile;
    private JButton findVhostFile;
    private JTextField phpFile;
    private JButton findPhpFile;
    private JTextField directoryPath;
    private JButton findDirectoryPath;
    private JTextField require;
    private JTextField allowOverride;
    private JTextField apachePath;
    private JButton findApacheBtn;
    private JPanel apachePane;
    private JCheckBox restartServer;
    private JButton accept;
    private JButton cancel;

    public PropertiesDialog(JFrame p, Properties prop, OperatingSystem os)
    {
        parent = p;
        properties = prop;

        setContentPane(configPane);
        getRootPane().setDefaultButton(accept);
        setPropertiesConfig();

        theme.addActionListener(e -> onThemeSelection());
        findHostsFile.addActionListener(e -> onFind(hostsFile, JFileChooser.FILES_ONLY));
        findVhostFile.addActionListener(e -> onFind(vhostsFile, JFileChooser.FILES_ONLY));
        findPhpFile.addActionListener(e -> onFind(phpFile, JFileChooser.FILES_ONLY));
        findDirectoryPath.addActionListener(e -> onFind(directoryPath, JFileChooser.DIRECTORIES_ONLY));
        findApacheBtn.addActionListener(e -> onFind(apachePath, JFileChooser.DIRECTORIES_ONLY));
        accept.addActionListener(e -> onAccept());
        cancel.addActionListener(e -> onCancel());

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

        // Call onCancel() on ESCAPE
        configPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if (os != OperatingSystem.WINDOWS) {
            apachePane.setVisible(false);
        }
    }

    public void addConfigListener(PropertiesListener listener)
    {
        propertiesListener = listener;
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/settings.png")).getImage());
        setTitle("Main configuration");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void setPropertiesConfig()
    {
        theme.setSelectedIndex(properties.getString("theme").equals("light") ? 0 : 1);
        hostsFile.setText(properties.getString("hosts_file"));
        vhostsFile.setText(properties.getString("vhosts_file"));
        phpFile.setText(properties.getString("php_file"));
        consoleLog.setSelected(properties.getBoolean("console_log"));
        directoryPath.setText(properties.getMainDirectory().getPath());
        require.setText(properties.getMainDirectory().getRequire());
        allowOverride.setText(properties.getMainDirectory().getAllowOverride());
        apachePath.setText(properties.getString("apache_path"));
        restartServer.setSelected(properties.getBoolean("restart_server"));
    }

    protected void onThemeSelection()
    {
        JOptionPane.showMessageDialog(
            this,
            "The theme will be updated when the application restarts.",
            "Theme changed",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void onFind(JTextField field, int mode)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setCurrentDirectory(new File(field.getText()));
        int selection = fileChooser.showOpenDialog(field);

        if (selection == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    protected void onAccept()
    {
        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("theme", theme.getSelectedItem().toString().toLowerCase());
        propertiesMap.put("hosts_file", hostsFile.getText());
        propertiesMap.put("vhosts_file", vhostsFile.getText());
        propertiesMap.put("php_file", phpFile.getText());
        propertiesMap.put("console_log", consoleLog.isSelected() ? "1" : "0");
        propertiesMap.put("directory_path", directoryPath.getText());
        propertiesMap.put("directory_require", require.getText());
        propertiesMap.put("directory_allow_override", allowOverride.getText());
        propertiesMap.put("apache_path", apachePath.getText());
        propertiesMap.put("restart_server", restartServer.isSelected() ? "1" : "0");

        propertiesListener.onPropertiesUpdate(propertiesMap);
        dispose();
    }

    protected void onCancel()
    {
        dispose();
    }
}
