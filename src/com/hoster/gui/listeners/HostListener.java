package com.hoster.gui.listeners;

import com.hoster.Host;

public interface HostListener
{
    void onHostAdded(Host host);
    void onHostDeleted();
    void onVirtualHostUpdated(Host host, int row);
}
