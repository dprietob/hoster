package com.hoster.data;

import java.util.ArrayList;
import java.util.Collection;

public class HostList extends ArrayList<Host> implements HList
{
    @Override
    public Host getEquals(Host h)
    {
        for (Host host : this) {
            if (host.equals(h)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public Collection<Host> diff(Collection<Host> hl)
    {
        Collection<Host> c = new ArrayList<>();
        for (Host h : this) {
            if (!hl.contains(h)) {
                c.add(h);
            }
        }
        return c;
    }
}
