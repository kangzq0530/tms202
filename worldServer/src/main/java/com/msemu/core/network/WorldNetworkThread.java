package com.msemu.core.network;

import com.msemu.core.startup.StartupComponent;
import com.msemu.world.World;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/18.
 */

@StartupComponent("Network")
public class WorldNetworkThread {

    private static final AtomicReference<WorldNetworkThread> instance = new AtomicReference<>();

    private final Map<Integer, ChannelNetworkThread> channelNetworkThreads = new ConcurrentHashMap<>();

    public static WorldNetworkThread getInstance() {
        WorldNetworkThread value = instance.get();
        if (value == null) {
            synchronized (WorldNetworkThread.instance) {
                value = instance.get();
                if (value == null) {
                    value = new WorldNetworkThread();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public WorldNetworkThread() {

    }


    public void startup() throws IOException, InterruptedException {
        World.getInstance().getChannels().forEach(ch-> addChannelThread(ch.getChannelId(), ch.getHost(), ch.getPort()));
        for (ChannelNetworkThread thread : channelNetworkThreads.values()) {
            thread.startup();
        }
    }

    public void addChannelThread(int channel, String host, int port) {
        if (channelNetworkThreads.containsKey(channel)) {
            throw new RuntimeException("channelThread is exists");
        }
        channelNetworkThreads.put(channel, new ChannelNetworkThread(host, port));
    }
}
