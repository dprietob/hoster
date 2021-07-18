package com.hoster.data;

import java.util.Collection;
import java.util.List;

public interface HList extends List<Host>
{
    Host getEquals(Host h);

    Collection<Host> diff(Collection<Host> hl);
}
