package com.hoster;

public class Host
{
    private final String DEFAULT_PORT = "80";

    private boolean active;
    private String ip;
    private String domain;
    private String serverAdmin;
    private String serverName;
    private String documentRoot;
    private String serverAlias;
    private String port;
    private String errorLog;
    private String customLog;
    private String require;
    private String allowOverride;

    public Host()
    {
        active = true;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getServerAdmin()
    {
        return serverAdmin;
    }

    public void setServerAdmin(String serverAdmin)
    {
        this.serverAdmin = serverAdmin;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getDocumentRoot()
    {
        return documentRoot;
    }

    public void setDocumentRoot(String documentRoot)
    {
        this.documentRoot = documentRoot;
    }

    public String getServerAlias()
    {
        return serverAlias;
    }

    public void setServerAlias(String serverAlias)
    {
        this.serverAlias = serverAlias;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getErrorLog()
    {
        return errorLog;
    }

    public void setErrorLog(String errorLog)
    {
        this.errorLog = errorLog;
    }

    public String getCustomLog()
    {
        return customLog;
    }

    public void setCustomLog(String customLog)
    {
        this.customLog = customLog;
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

    // TODO
    public String parseToXML()
    {
        return null;
    }
}
