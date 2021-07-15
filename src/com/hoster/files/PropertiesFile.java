package com.hoster.files;

import com.hoster.data.Properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PropertiesFile extends ConfigFile
{
    private static final String CONFIG_FILE = "config.properties";


    public static Map<String, Object> load()
    {
        Map<String, Object> initialConfig = getEmptyProperties();
        try {
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String[] line = myReader.nextLine().split("=");
                    if (line.length == 2) {
                        initialConfig.put(line[0].trim(), line[1].trim());
                    }
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return initialConfig;
    }

    public static boolean save(Properties properties)
    {
        if (deleteCurrentFile(CONFIG_FILE)) {
            if (createNewFile(CONFIG_FILE)) {
                return writeFile(properties);
            }
        }
        return false;
    }

    private static boolean writeFile(Properties properties)
    {
        try {
            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
            fileWriter.write("# Do not modify manually! \n");
            for (Map.Entry<String, Object> entry : properties.getPropertiesMap().entrySet()) {
                fileWriter.write(entry.getKey() + "=" + entry.getValue().toString() + "\n");
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Map<String, Object> getEmptyProperties()
    {
        Map<String, Object> properties = new HashMap<>();
        properties.put("theme", "light");
        properties.put("hosts_file", "");
        properties.put("vhosts_file", "");
        properties.put("directory_path", "");
        properties.put("directory_require", "");
        properties.put("directory_allow_override", "");
        properties.put("apache_path", "");
        properties.put("restart_server", "0");

        return properties;
    }
}
