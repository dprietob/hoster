package com.hoster.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class Directory
{
    private String path;
    private String require;
    private String allowOverride;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getRequire()
    {
        return require;
    }

    public void setRequire(String require)
    {
        this.require = require;
    }

    public String getAllowOverride()
    {
        return allowOverride;
    }

    public void setAllowOverride(String allowOverride)
    {
        this.allowOverride = allowOverride;
    }

    public boolean isValid()
    {
        return (getRequire() != null && !getRequire().isEmpty())
            || (getAllowOverride() != null && !getAllowOverride().isEmpty());
    }

    public String parseToXML()
    {
        return parseToXML(false);
    }

    public String parseToXML(boolean extraTab)
    {
        if (isValid()) {
            StringBuilder out = new StringBuilder();
            Map<String, String> data = new LinkedHashMap<>();

            data.put("AllowOverride", getAllowOverride());
            data.put("Require", getRequire());

            if (extraTab) {
                out.append("\t");
            }

            out.append("<Directory \"").append(getPath()).append("\">\n");
            insertTagsToXML(out, data, extraTab);

            if (extraTab) {
                out.append("\t");
            }

            out.append("</Directory>");

            return out.toString();
        }
        return "";
    }

    private void insertTagsToXML(StringBuilder out, Map<String, String> data, boolean extraTab)
    {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                if (extraTab) {
                    out.append("\t");
                }

                out.append("\t").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }
        }
    }
}
