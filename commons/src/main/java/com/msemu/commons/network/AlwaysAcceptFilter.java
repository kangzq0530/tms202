package com.msemu.commons.network;

import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/14.
 */
public class AlwaysAcceptFilter implements IAcceptFilter {
    public AlwaysAcceptFilter() {
    }

    public boolean isAllowedAddress(InetSocketAddress address) {
        return true;
    }
}
