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


    public Map<String, Object> load()
    {
        Map<String, Object> initialConfig = getEmptyProperties();
        File file = new File(CONFIG_FILE);

        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                String[] defSplit;

                while (reader.hasNextLine()) {
                    defSplit = reader.nextLine().split("=");
                    if (defSplit.length == 2) {
                        initialConfig.put(cleanString(defSplit[0]), cleanString(defSplit[1]));
                    }
                }
                reader.close();
            }
        } catch (FileNotFoundException e) {
            // TODO: consoleListener is null on main() call
            consoleListener.onConsoleError(e.getMessage());
        }

        return initialConfig;
    }

    public boolean save(Properties properties)
    {
        if (deleteCurrentFile(CONFIG_FILE)) {
            if (createNewFile(CONFIG_FILE)) {
                return writeFile(properties);
            }
        }
        return false;
    }

    private boolean writeFile(Properties properties)
    {
        try {
            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
            fileWriter.write("# Do not modify manually! \n\n");
            for (Map.Entry<String, Object> entry : properties.getPropertiesMap().entrySet()) {
                fileWriter.write(entry.getKey() + "=" + entry.getValue().toString() + "\n");
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            consoleListener.onConsoleError(e.getMessage());
        }
        return false;
    }

    private Map<String, Object> getEmptyProperties()
    {
        Map<String, Object> properties = new HashMap<>();
        properties.put("theme", "light");
        properties.put("hosts_file", "");
        properties.put("vhosts_file", "");
        properties.put("php_file", "");
        properties.put("console_log", "0");
        properties.put("directory_path", "");
        properties.put("directory_require", "");
        properties.put("directory_allow_override", "");
        properties.put("apache_path", "");
        properties.put("restart_server", "0");

        return properties;
    }
}
