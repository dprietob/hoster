package com.hoster;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.data.HList;
import com.hoster.data.Host;
import com.hoster.data.Properties;
import com.hoster.files.HostsFile;
import com.hoster.files.PropertiesFile;
import com.hoster.files.VHostsFile;
import com.hoster.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            PropertiesFile propertiesFile = new PropertiesFile();
            HostsFile hostsFile = new HostsFile();
            VHostsFile vHostsFile = new VHostsFile();

            Properties properties = new Properties(propertiesFile, hostsFile, vHostsFile);
            HList hostsList = mergeHostLists(
                hostsFile.load(properties.getString("hosts_file")),
                vHostsFile.load(properties.getString("vhosts_file"))
            );

            setTheme(properties.getString("theme"));
            MainFrame frame = new MainFrame(properties, hostsList);
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

    private static HList mergeHostLists(HList hosts, HList vhosts)
    {
        Host h;
        for (Host vh : vhosts) {
            h = hosts.getEquals(vh);
            if (h != null) {
                vh.setIp(h.getIp());
                vh.setActive(h.isActive());
            }
        }
        /*
         * Adds the rest of the hosts defined in the hosts file that are
         * not defined in the vhosts file.
         */
        vhosts.addAll(hosts.diff(vhosts));
        return vhosts;
    }
}
