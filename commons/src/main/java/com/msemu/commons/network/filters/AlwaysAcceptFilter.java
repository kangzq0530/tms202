package com.msemu.commons.network.filters;

import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/29.
 */
public class AlwaysAcceptFilter implements IAcceptFilter {
    @Override
    public boolean isAllowedAddress(InetSocketAddress socketAddress) {
        return true;
    }
}
