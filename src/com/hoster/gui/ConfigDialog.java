package com.hoster.gui;

import com.hoster.Properties;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class ConfigDialog extends JDialog
{
    private Map<String, String> properties;
    private JFrame parent;
    private JPanel configPane;
    private JComboBox theme;
    private JTextField domainsFile;
    private JButton findDomainsFile;
    private JTextField vhostsFile;
    private JButton findVhostFile;
    private JTextField directoryPath;
    private JButton findDirectoryPath;
    private JTextField require;
    private JTextField allowOverride;
    private JButton accept;
    private JButton cancel;

    public ConfigDialog(JFrame p, Map<String, String> config)
    {
        parent = p;
        properties = config;

        setContentPane(configPane);
        getRootPane().setDefaultButton(accept);
        setPropertiesConfig();

        theme.addActionListener(e -> onThemeSelection());
        findDomainsFile.addActionListener(e -> onFind(domainsFile, JFileChooser.FILES_ONLY));
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

    private void setPropertiesConfig()
    {
        theme.setSelectedIndex(properties.get("theme").equals("light") ? 0 : 1);
        domainsFile.setText(properties.get("domains_file"));
        vhostsFile.setText(properties.get("vhost_file"));
        directoryPath.setText(properties.get("directory_path"));
        require.setText(properties.get("directory_require"));
        allowOverride.setText(properties.get("directory_allow_override"));
    }

    private void onThemeSelection()
    {
        JOptionPane.showMessageDialog(
            this,
            "Theme settings will be applied when the application is restarted.",
            "Theme changed",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void onFind(JTextField field, int mode)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(mode);
        int selection = fileChooser.showOpenDialog(field);
        if (selection == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void onAccept()
    {
        properties.clear();
        properties.put("theme", theme.getSelectedItem().toString().toLowerCase());
        properties.put("domains_file", domainsFile.getText());
        properties.put("vhost_file", vhostsFile.getText());
        properties.put("directory_path", directoryPath.getText());
        properties.put("directory_require", require.getText());
        properties.put("directory_allow_override", allowOverride.getText());

        if (Properties.save(properties)) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "An error occurred while trying to save the properties file. Please make sure the application has the necessary privileges.",
                "Properties file error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel()
    {
        dispose();
    }
}
