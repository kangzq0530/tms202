package com.msemu.world.network.netty;

import com.msemu.commons.network.netty.IClientFactory;
import com.msemu.commons.utils.Rand;
import com.msemu.world.client.GameClient;
import io.netty.channel.Channel;

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
    public GameClient createClient(Channel channel) {
        byte[] siv = new byte[4];
        byte[] riv = new byte[4];
        Rand.nextBytes(siv);
        Rand.nextBytes(riv);
        return new GameClient(channel, siv, riv);
    }
}
