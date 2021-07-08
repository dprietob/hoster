package com.hoster.data;

import java.util.HashMap;
import java.util.Map;

public class Properties
{
    private Map<String, Object> propertiesMap;
    private Map<String, Object> properties;

    public Properties(Map<String, Object> map)
    {
        propertiesMap = map;
        properties = new HashMap<>();
        parseToProperties();
    }

    public Map<String, Object> getPropertiesMap()
    {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, Object> map)
    {
        propertiesMap = map;
        parseToProperties();
    }

    public String getString(String key)
    {
        return properties.get(key).toString();
    }

    public boolean getBoolean(String key)
    {
        return (boolean)properties.get(key);
    }

    public Directory getMainDirectory()
    {
        return (Directory) properties.get("main_directory");
    }

    private void parseToProperties()
    {
        Directory mainDirectory = new Directory();
        mainDirectory.setPath(propertiesMap.get("directory_path").toString());
        mainDirectory.setRequire(propertiesMap.get("directory_require").toString());
        mainDirectory.setAllowOverride(propertiesMap.get("directory_allow_override").toString());

        properties.clear();
        properties.put("theme", propertiesMap.get("theme"));
        properties.put("hosts_file", propertiesMap.get("hosts_file"));
        properties.put("vhost_file", propertiesMap.get("vhost_file"));
        properties.put("restart_server", propertiesMap.get("restart_server").equals("1"));
        properties.put("restart_server_command", propertiesMap.get("restart_server_command"));
        properties.put("main_directory", mainDirectory);
    }
}
