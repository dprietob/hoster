package com.hoster;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.data.Host;
import com.hoster.data.Properties;
import com.hoster.files.HostsFile;
import com.hoster.files.PropertiesFile;
import com.hoster.gui.HostFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Properties properties = new Properties(PropertiesFile.load());
            List<Host> hostsList = HostsFile.load(properties.getString("hosts_file"));

            setTheme(properties.getString("theme"));
            HostFrame frame = new HostFrame(hostsList, properties);
            frame.build();
        });
    }

    private static void setTheme(String themeType)
    {
        try {
            if (themeType.equals("dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
