package com.hoster.files;

import com.hoster.Host;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HostsFile extends ConfigFile
{
    public static List<Host> load(String fileName)
    {
        List<Host> hostsList = new ArrayList<>();

        try {
            Host h;
            File file = new File(fileName);
            if (file.exists()) {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String[] line = myReader.nextLine().split(" ");
                    if (line.length == 2) {
                        h = new Host();
                        h.setActive(isHostActive(line[0]));
                        h.setIp(line[0]);
                        h.setDomain(line[1]);

                        hostsList.add(h);
                    }
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return hostsList;
    }

    public static boolean save(String fileName, List<Host> hostsList, String appName, String appVersion)
    {
        if (deleteCurrentFile(fileName)) {
            if (createNewFile(fileName)) {
                return writeFile(fileName, hostsList, appName, appVersion);
            }
        }
        return false;
    }

    private static boolean writeFile(String fileName, List<Host> hostsList, String appName, String appVersion)
    {
        try {
            if (!hostsList.isEmpty()) {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("# Generated by " + appName + " " + appVersion + "\n");
                String ip;

                for (Host h : hostsList) {
                    ip = (!h.isActive() ? "#" : "") + h.getIp();
                    fileWriter.write(ip + " " + h.getDomain() + "\n");
                }
                fileWriter.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isHostActive(String ip)
    {
        return ip.charAt(0) != '#';
    }
}