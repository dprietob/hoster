package com.hoster.gui.listeners;

import java.util.Map;

public interface PropertiesListener
{
    void onPropertiesUpdate(Map<String, Object> propertiesMap);
    void onShowConsoleLog(boolean show);
}
