package com.hoster.data;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private Directory directory;

    public Host()
    {
        directory = new Directory();
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
        directory.setPath(documentRoot);
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

    public Directory getDirectory()
    {
        return directory;
    }

    public void setDirectory(Directory directory)
    {
        this.directory = directory;
    }

    public boolean isValid()
    {
        return getDocumentRoot() != null
            && !getDocumentRoot().isEmpty()
            && getServerName() != null
            && !getServerName().isEmpty();
    }

    public String parseToXML()
    {
        if (isValid()) {
            String port = getPort() != null ? getPort() : DEFAULT_PORT;
            StringBuilder out = new StringBuilder();
            Map<String, String> data = new LinkedHashMap<>();
            Directory directory = getDirectory();

            data.put("ServerAdmin", getServerAdmin());
            data.put("DocumentRoot", getDocumentRoot());
            data.put("ServerName", getServerName());
            data.put("ServerAlias", getServerAlias());
            data.put("ErrorLog", getErrorLog());
            data.put("CustomLog", getCustomLog());

            out.append("<VirtualHost *:").append(port).append(">").append("\n");
            insertTagsToXML(out, data);
            out.append(directory.parseToXML(true));
            out.append("</VirtualHost>").append("\n");

            return out.toString();
        }
        return "";
    }

    private void insertTagsToXML(StringBuilder out, Map<String, String> data)
    {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                out.append("\t").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }
        }
    }
}
