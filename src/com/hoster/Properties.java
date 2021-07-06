package com.hoster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Properties
{
    private static final String CONFIG_FILE = "config.properties";

    public static Map<String, String> load()
    {
        Map<String, String> properties = getEmptyProperties();

        try {
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String[] line = myReader.nextLine().split("=");
                    if (line.length == 2) {
                        properties.put(line[0].trim(), line[1].trim());
                    }
                }
                myReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return properties;
    }

    public static boolean save(Map<String, String> config)
    {
        if (deleteCurrentPropertiesFile()) {
            if (createNewPropertiesFile()) {
                return writePropertiesFile(config);
            }
        }
        return false;
    }

    private static Map<String, String> getEmptyProperties()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("directory_path", "");
        properties.put("directory_require", "");
        properties.put("directory_allow_override", "");
        properties.put("theme", "light");
        properties.put("domains_file", "");
        properties.put("vhost_file", "");

        return properties;
    }

    private static boolean deleteCurrentPropertiesFile()
    {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    private static boolean createNewPropertiesFile()
    {
        try {
            File file = new File(CONFIG_FILE);
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writePropertiesFile(Map<String, String> config)
    {
        try {
            if(!config.isEmpty()) {
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
}
