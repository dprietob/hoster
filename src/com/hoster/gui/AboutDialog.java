package com.hoster.gui;

import com.hoster.gui.listeners.ConsoleListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class AboutDialog extends JDialog implements MouseListener
{
    private final String DEVELOPER_URL = "https://github.com/dprietob";
    private final String FLATLAF_URL = "https://github.com/DevCharly";
    private final String FARMFRESH_URL = "https://www.fatcow.com/free-icons";
    private final String REPORT_URL = "https://github.com/dprietob/hoster/issues";
    private final Color LINK_COLOR = new Color(42, 155, 187);

    private ConsoleListener consoleListener;
    private JFrame parent;
    private JPanel aboutPane;
    private JLabel developerLink;
    private JLabel flatlafLink;
    private JLabel farmFreshLink;
    private JButton reportBug;
    private JButton close;


    public AboutDialog(JFrame p)
    {
        setContentPane(aboutPane);
        getRootPane().setDefaultButton(close);

        parent = p;
        developerLink.addMouseListener(this);
        flatlafLink.addMouseListener(this);
        farmFreshLink.addMouseListener(this);
        reportBug.addActionListener(e -> onReportBug());
        close.addActionListener(e -> onClose());

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onClose();
            }
        });

        // Call onCancel() on ESCAPE
        aboutPane.registerKeyboardAction(e -> onClose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addConsoleListener(ConsoleListener listener)
    {
        consoleListener = listener;
    }

    public void build()
    {
        pack();
        setIconImage(new ImageIcon(getClass().getResource("icons/help.png")).getImage());
        setTitle("About");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    private boolean openWebpage(String url)
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(url).toURI());
                return true;
            } catch (Exception e) {
                consoleListener.onConsoleError(e.getMessage());
            }
        }
        return false;
    }

    protected void onReportBug()
    {
        openWebpage(REPORT_URL);
    }

    protected void onClose()
    {
        dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        JLabel label = (JLabel) e.getSource();

        if (label.equals(developerLink)) {
            openWebpage(DEVELOPER_URL);

        } else if (label.equals(flatlafLink)) {
            openWebpage(FLATLAF_URL);

        } else if (label.equals(farmFreshLink)) {
            openWebpage(FARMFRESH_URL);
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(label.getForeground().brighter());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(LINK_COLOR);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
