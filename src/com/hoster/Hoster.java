package com.hoster;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.files.HostsFile;
import com.hoster.files.PropertiesFile;
import com.hoster.gui.HostFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Map<String, String> propertiesMap = PropertiesFile.load();
            List<Host> hostsList = HostsFile.load(propertiesMap.get("hosts_file"));

            setTheme(propertiesMap.get("theme"));
            HostFrame frame = new HostFrame(hostsList, propertiesMap);
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
