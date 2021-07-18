package com.hoster.files;

import com.hoster.data.HList;
import com.hoster.data.Host;
import com.hoster.data.HostList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HostsFile extends ConfigFile
{
    public HList load(String fileName)
    {
        HList hostsList = new HostList();
        File file = new File(fileName);
        Host h;

        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String[] line = reader.nextLine().split(" ");
                    if (line.length == 2) {
                        h = new Host();
                        h.setActive(isHostActive(line[0].trim()));
                        h.setIp(line[0].trim().replaceAll("#", ""));
                        h.setServerName(line[1].trim());

                        hostsList.add(h);
                    }
                }
                reader.close();
            } else {
                // TODO
            }
        } catch (FileNotFoundException e) {
            // TODO: consoleListener is null on main() call
            consoleListener.onConsoleError(e.getMessage());
        }

        return hostsList;
    }

    public boolean save(String fileName, HList hostsList, String appName, String appVersion)
    {
        if (deleteCurrentFile(fileName)) {
            if (createNewFile(fileName)) {
                return writeFile(fileName, hostsList, appName, appVersion);
            }
        }
        return false;
    }

    private boolean writeFile(String fileName, HList hostsList, String appName, String appVersion)
    {
        try {
            if (!hostsList.isEmpty()) {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("# Generated by " + appName + " " + appVersion + "\n");
                String ip;

                for (Host h : hostsList) {
                    ip = (!h.isActive() ? "#" : "") + h.getIp();
                    fileWriter.write(ip + " " + h.getServerName() + "\n");
                }
                fileWriter.close();
                return true;
            }
        } catch (IOException e) {
            // TODO: consoleListener is null on main() call
            consoleListener.onConsoleError(e.getMessage());
        }
        return false;
    }

    private boolean isHostActive(String ip)
    {
        return ip.charAt(0) != '#';
    }
}
