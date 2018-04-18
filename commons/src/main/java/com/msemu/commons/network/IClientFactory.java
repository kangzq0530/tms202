package com.msemu.commons.network;

/**
 * Created by Weber on 2018/3/29.
 */
public interface IClientFactory<TClient extends Client<TClient>> {
    TClient createClient(Connection<TClient> connection);
}
