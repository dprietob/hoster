package com.hoster;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.files.Hosts;
import com.hoster.files.Properties;
import com.hoster.gui.HostFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Map<String, String> properties = Properties.load();
            Map<String, String> hosts = Hosts.load(properties.get("hosts_file"));

            setTheme(properties.get("theme"));
            HostFrame frame = new HostFrame(hosts, properties);
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
