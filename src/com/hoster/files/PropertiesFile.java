package com.hoster.files;

import java.util.HashMap;
import java.util.Map;

public class PropertiesFile extends ConfigFile
{
    private static final String CONFIG_FILE = "config.properties";

    public static Map<String, String> load()
    {
        return new ConfigFile().load(CONFIG_FILE, getEmptyProperties(), "=");
    }

    public static boolean save(Map<String, String> config)
    {
        return new ConfigFile().save(CONFIG_FILE, "Do not modify manually!", "=", config);
    }

    private static Map<String, String> getEmptyProperties()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("directory_path", "");
        properties.put("directory_require", "");
        properties.put("directory_allow_override", "");
        properties.put("theme", "light");
        properties.put("hosts_file", "");
        properties.put("vhost_file", "");

        return properties;
    }
}
