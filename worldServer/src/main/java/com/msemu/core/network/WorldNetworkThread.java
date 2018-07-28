/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

    public WorldNetworkThread() {

    }

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

    public void startup() throws IOException, InterruptedException {
        World.getInstance().getChannels().forEach(ch -> addChannelThread(ch.getChannelId(), ch.getHost(), ch.getPort()));
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
