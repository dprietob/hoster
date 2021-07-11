package com.hoster.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Server
{
    private static final String LINUX_STATUS_COMMAND = "service apache2 status";
    private static final String LINUX_RESTART_COMMAND = "service apache2 restart";

    private static final String WINDOWS_STATUS_COMMAND = "tasklist /fi \"imagename eq httpd.exe\"";
    private static final String WINDOWS_RESTART_COMMAND = "C:\\xampp\\apache\\bin\\httpd -k restart";
    private static final String WINDOWS_INSTALL_COMMAND = "C:\\xampp\\apache\\bin\\httpd -k install";

    public static void restart()
    {
        switch (getOS()) {
            case LINUX:
                exeCommand(LINUX_RESTART_COMMAND);
                break;
            case WINDOWS:
                restartWindowsServer();
                break;
            case MAC:
                // TODO
                break;
            default:
                // TODO
        }
    }

    private static void restartWindowsServer()
    {
        System.out.println("iniciando servicio windows");
        String cmdOutput = getCommandOutput(WINDOWS_RESTART_COMMAND);
        System.out.println(cmdOutput);
        if (cmdOutput != null && cmdOutput.contains("No installed service")) {
            System.out.println("Servicio no instalado");
            exeCommand(WINDOWS_INSTALL_COMMAND);
            exeCommand(WINDOWS_RESTART_COMMAND);
        }
    }

    public static boolean isActive()
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

    private static boolean isLinuxServerActive()
    {
        String cmdOutput = getCommandOutput(LINUX_STATUS_COMMAND);

        return cmdOutput != null
            && cmdOutput.contains("Active: active (running)");
    }

    private static boolean isWindowsServerActive()
    {
        String cmdOutput = getCommandOutput(WINDOWS_STATUS_COMMAND);
        return cmdOutput != null && cmdOutput.contains("httpd.exe");
    }

    private static boolean isMacServerActive()
    {
        // TODO
        return false;
    }

    private static void exeCommand(String cmd)
    {
        try {
            System.out.println(cmd);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCommandOutput(String cmd)
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
            e.printStackTrace();
        }
        return null;
    }

    private static OperatingSystem getOS()
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