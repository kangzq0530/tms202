package com.msemu.login.modules;

import com.msemu.commons.network.filters.IAcceptFilter;

import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/15.
 */

public class FirewallFilter implements IAcceptFilter {

    public boolean isAllowedAddress(InetSocketAddress inetSocketAddress) {
        // TODO: Implement firrewall
        return true;
    }
}