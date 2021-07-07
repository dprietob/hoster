package com.hoster.files;

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


    public static Map<String, String> load()
    {
        Map<String, String> initialConfig = getEmptyProperties();
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

    public static boolean save(Map<String, String> config)
    {
        if (deleteCurrentFile(CONFIG_FILE)) {
            if (createNewFile(CONFIG_FILE)) {
                return writeFile(config);
            }
        }
        return false;
    }

    private static boolean writeFile(Map<String, String> config)
    {
        try {
            if (!config.isEmpty()) {
                FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                fileWriter.write("# Do not modify manually! \n");
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    fileWriter.write(entry.getKey() + "=" + entry.getValue() + "\n");
                }
                fileWriter.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Map<String, String> getEmptyProperties()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("theme", "light");
        properties.put("hosts_file", "");
        properties.put("vhost_file", "");
        properties.put("directory_path", "");
        properties.put("directory_require", "");
        properties.put("directory_allow_override", "");

        return properties;
    }
}
