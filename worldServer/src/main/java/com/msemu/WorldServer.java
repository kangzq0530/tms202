package com.msemu;

import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.core.startup.StartupLevel;
import com.msemu.core.startup.StartupManager;
import com.msemu.world.service.WorldServerRMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/16.
 */
public class WorldServer {
    private static final Logger log = LoggerFactory.getLogger(WorldServer.class);
    private static WorldServerRMI rmi;

    public WorldServer() throws Exception {
        rmi = new WorldServerRMI();
        StartupManager.getInstance().startup(StartupLevel.class);
    }

    public static void main(final String[] args) {
        try {
            new WorldServer();
        } catch (Exception ex) {
            WorldServer.log.error("Error while starting MainServer", ex);
        }
    }

    public static WorldServerRMI getRmi() {
        return rmi;
    }
}
