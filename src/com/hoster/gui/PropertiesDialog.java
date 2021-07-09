package com.hoster.gui;

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
    private JTextField vhostsFile;
    private JButton findVhostFile;
    private JTextField directoryPath;
    private JButton findDirectoryPath;
    private JTextField require;
    private JTextField allowOverride;
    private JTextField restartCommand;
    private JCheckBox restartServer;
    private JButton accept;
    private JButton cancel;
    private JTextField statusCommand;

    public PropertiesDialog(JFrame p, Properties prop)
    {
        parent = p;
        properties = prop;

        setContentPane(configPane);
        getRootPane().setDefaultButton(accept);
        setPropertiesConfig();

        theme.addActionListener(e -> onThemeSelection());
        findHostsFile.addActionListener(e -> onFind(hostsFile, JFileChooser.FILES_ONLY));
        findVhostFile.addActionListener(e -> onFind(vhostsFile, JFileChooser.FILES_ONLY));
        findDirectoryPath.addActionListener(e -> onFind(directoryPath, JFileChooser.DIRECTORIES_ONLY));
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
        vhostsFile.setText(properties.getString("vhost_file"));
        directoryPath.setText(properties.getMainDirectory().getPath());
        require.setText(properties.getMainDirectory().getRequire());
        allowOverride.setText(properties.getMainDirectory().getAllowOverride());
        restartServer.setSelected(properties.getBoolean("restart_server"));
        restartCommand.setText(properties.getString("restart_server_command"));
    }

    protected void onThemeSelection()
    {
        JOptionPane.showMessageDialog(
            this,
            "The theme will be updated when the application is restarted.",
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
        propertiesMap.put("vhost_file", vhostsFile.getText());
        propertiesMap.put("directory_path", directoryPath.getText());
        propertiesMap.put("directory_require", require.getText());
        propertiesMap.put("directory_allow_override", allowOverride.getText());
        propertiesMap.put("restart_server", restartServer.isSelected() ? "1" : "0");
        propertiesMap.put("restart_server_command", restartCommand.getText());

        propertiesListener.onPropertiesUpdate(propertiesMap);
        dispose();
    }

    protected void onCancel()
    {
        dispose();
    }
}
