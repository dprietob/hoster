package com.hoster.data;

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
}
