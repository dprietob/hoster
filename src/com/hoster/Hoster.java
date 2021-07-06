package com.hoster;

import com.formdev.flatlaf.FlatLightLaf;
import com.hoster.gui.DomainFrame;

import javax.swing.*;
import java.awt.*;

public class Hoster
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                DomainFrame frame = new DomainFrame();
                frame.build();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
    }
}
