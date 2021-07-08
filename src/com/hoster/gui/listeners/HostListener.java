package com.hoster.gui.listeners;

import com.hoster.Host;

public interface HostListener
{
    void onHostAdded(Host host);
    void onHostEdited(Host host, int row);
    void onHostDeleted();
    void onVirtualHostUpdated(Host host, int row);
}
