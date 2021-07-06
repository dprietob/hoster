package com.hoster;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.gui.DomainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try {
                Map<String, String> config = Properties.load();
                if (config.get("theme").equals("dark")) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                DomainFrame frame = new DomainFrame(config);
                frame.build();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
    }
}
