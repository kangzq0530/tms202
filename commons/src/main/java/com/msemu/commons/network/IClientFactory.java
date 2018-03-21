package com.msemu.commons.network;

/**
 * Created by Weber on 2018/3/14.
 */

public interface IClientFactory<TClient extends Client<TClient>> {
    TClient createClient(Connection<TClient> var1);
}