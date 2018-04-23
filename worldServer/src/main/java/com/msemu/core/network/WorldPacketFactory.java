package com.msemu.core.network;

import com.msemu.commons.network.handler.PropertiesPacketHandlerFactory;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/18.
 */
@StartupComponent("Network")
public class WorldPacketFactory extends PropertiesPacketHandlerFactory<GameClient> {

    private static final AtomicReference<Object> instance = new AtomicReference<>();

    public static WorldPacketFactory getInstance() {
        Object o = instance.get();
        if (o == null) {
            synchronized (WorldPacketFactory.instance) {
                o = instance.get();
                if (o == null) {
                    o = new WorldPacketFactory();
                }
                instance.set(o);
            }
        }
        return (WorldPacketFactory) ((o == WorldPacketFactory.instance) ? null : o);
    }
}
