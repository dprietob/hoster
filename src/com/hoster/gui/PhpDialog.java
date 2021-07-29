package com.hoster.gui;

import com.hoster.data.Properties;
import com.hoster.gui.listeners.PropertiesListener;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PhpDialog extends JDialog
{
    private Properties properties;
    private Ini ini;
    private PropertiesListener propertiesListener;

    private JFrame parent;
    private JPanel phpPane;
    private JButton cancel;
    private JButton accept;

    public PhpDialog(JFrame p, Properties prop)
    {
        parent = p;
        properties = prop;

        setContentPane(phpPane);
        getRootPane().setDefaultButton(accept);
        setPhpConfig();

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
        phpPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addConfigListener(PropertiesListener listener)
    {
        propertiesListener = listener;
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/php.png")).getImage());
        setTitle("PHP configuration");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    protected void setPhpConfig()
    {
        // TODO
        try {
            ini = new Ini(new File(properties.getString("php_file")));
            getSectionsList();
//            System.out.println(ini.get("PHP", "engine"));
//            System.out.println(ini.getAll("PHP"));

//            ini.put("PHP", "engine", "off");
//            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String[] getSectionsList()
    {
        System.out.println("Number of sections: " + ini.size() + "\n");
        for (String sectionName : ini.keySet()) {
            System.out.println("[" + sectionName + "]");
            Profile.Section section = ini.get(sectionName);
            for (String optionKey : section.keySet()) {
                System.out.println("\t" + optionKey + "=" + section.get(optionKey));
            }
        }
        return null;
    }

    protected void onAccept()
    {
        Map<String, Object> phpMap = new HashMap<>();
//        phpMap.put("theme", theme.getSelectedItem().toString().toLowerCase());
//        phpMap.put("hosts_file", hostsFile.getText());
//        phpMap.put("vhosts_file", vhostsFile.getText());
//        phpMap.put("php_file", phpFile.getText());
//        phpMap.put("console_log", consoleLog.isSelected() ? "1" : "0");
//        phpMap.put("directory_path", directoryPath.getText());
//        phpMap.put("directory_require", require.getText());
//        phpMap.put("directory_allow_override", allowOverride.getText());
//        phpMap.put("apache_path", apachePath.getText());
//        phpMap.put("restart_server", restartServer.isSelected() ? "1" : "0");

        propertiesListener.onPhpUpdate(phpMap);
        dispose();
    }

    protected void onCancel()
    {
        dispose();
    }
}
