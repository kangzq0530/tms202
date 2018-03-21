package com.msemu.commons.network;

import java.net.InetSocketAddress;

/**
 * Created by Weber on 2018/3/14.
 */
public interface IAcceptFilter {
    boolean isAllowedAddress(InetSocketAddress var1);
}
