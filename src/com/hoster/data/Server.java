package com.hoster.data;

import com.hoster.gui.listeners.ConsoleListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Server
{
    private static final String LINUX_STATUS_COMMAND = "service apache2 status";
    private static final String LINUX_RESTART_COMMAND = "service apache2 restart";

    private static final String WINDOWS_STATUS_COMMAND = "tasklist /fi \"imagename eq httpd.exe\"";
    private static final String WINDOWS_RESTART_COMMAND = "\\bin\\httpd -k restart"; // runservice
    private static final String WINDOWS_INSTALL_COMMAND = "\\bin\\httpd -k install";

    private ConsoleListener consoleListener;

    public void addConsoleListener(ConsoleListener listener)
    {
        consoleListener = listener;
    }

    public void restart(String apachePath)
    {
        switch (getOS()) {
            case LINUX:
                exeCommand(LINUX_RESTART_COMMAND);
                break;
            case WINDOWS:
                restartWindowsServer(apachePath);
                break;
            case MAC:
                // TODO
                break;
            default:
                // TODO
        }
    }

    private void restartWindowsServer(String apachePath)
    {
        if (apachePath != null) {
            String cmdOutput = getCommandOutput(apachePath + WINDOWS_RESTART_COMMAND);
            if (cmdOutput != null && cmdOutput.contains("No installed service")) {
                exeCommand(WINDOWS_INSTALL_COMMAND);
                exeCommand(WINDOWS_RESTART_COMMAND);
            } else {
                consoleListener.onConsoleError("Unable to restart server: unknown restart response.");
            }
        } else {
            consoleListener.onConsoleError("Unable to restart server: Apache path is empty.");
        }
    }

    public boolean isActive()
    {
        switch (getOS()) {
            case LINUX:
                return isLinuxServerActive();
            case WINDOWS:
                return isWindowsServerActive();
            case MAC:
                return isMacServerActive();
        }
        return false;
    }

    private boolean isLinuxServerActive()
    {
        String cmdOutput = getCommandOutput(LINUX_STATUS_COMMAND);

        return cmdOutput != null
            && cmdOutput.contains("Active: active (running)");
    }

    private boolean isWindowsServerActive()
    {
        String cmdOutput = getCommandOutput(WINDOWS_STATUS_COMMAND);
        return cmdOutput != null && cmdOutput.contains("httpd.exe");
    }

    private boolean isMacServerActive()
    {
        // TODO
        return false;
    }

    private void exeCommand(String cmd)
    {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
        } catch (Exception e) {
            consoleListener.onConsoleError(e.getMessage());
        }
    }

    private String getCommandOutput(String cmd)
    {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);

            InputStream is = pr.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder scOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                scOutput.append(line).append("\n");
            }
            pr.waitFor();

            is.close();
            isr.close();
            br.close();

            return scOutput.toString();
        } catch (Exception e) {
            consoleListener.onConsoleError(e.getMessage());
        }
        return null;
    }

    public OperatingSystem getOS()
    {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows")) {
            return OperatingSystem.WINDOWS;

        } else if (osName.contains("linux")) {
            return OperatingSystem.LINUX;

        } else if (osName.contains("mac")) {
            return OperatingSystem.MAC;
        }

        return OperatingSystem.UNKNOW;
    }
}