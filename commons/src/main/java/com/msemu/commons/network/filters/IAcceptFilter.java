package com.msemu.commons.network.filters;

import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/29.
 */
public interface IAcceptFilter {
    boolean isAllowedAddress(InetSocketAddress socketAddress);
}
