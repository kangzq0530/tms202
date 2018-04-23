package com.msemu.core.network;

import com.msemu.commons.network.Connection;
import com.msemu.commons.network.IClientFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/30.
 */
public class GameClientFactory implements IClientFactory<GameClient> {

    private static final AtomicReference<GameClientFactory> instance = new AtomicReference<>();

    public static GameClientFactory getInstance() {
        GameClientFactory value = instance.get();
        if (value == null) {
            synchronized (GameClientFactory.instance) {
                value = instance.get();
                if (value == null) {
                    value = new GameClientFactory();
                }
                instance.set(value);
            }
        }
        return value;
    }

    @Override
    public GameClient createClient(Connection<GameClient> connection) {
        return new GameClient(connection);
    }
}
