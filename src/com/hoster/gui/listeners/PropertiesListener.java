package com.hoster.gui.listeners;

import java.util.Map;

public interface PropertiesListener
{
    void onPhpUpdate(Map<String, Object> phpMap);
    void onPropertiesUpdate(Map<String, Object> propertiesMap);
}
