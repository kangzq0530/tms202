package com.msemu.core.startup;

import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.commons.utils.versioning.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

/**
 * Created by Weber on 2018/3/17.
 */
public enum StartupLevel implements IStartupLevel {
    BeforeStart {
        public void invokeDepends() {
            Version.getInstance().init((Class) this.getClass());
        }
    },
    Configure,
    Event,
    Database,
    Service,
    Data,
    World,
    Network,
    AfterStart {
        public void invokeDepends() {

            System.gc();
            System.runFinalization();
            for (final String line : ServerInfoUtils.getMemUsage()) {
                StartupLevel.log.info(line);
            }
//            try {
//                //GameNetworkThread.getInstance().startup();
//            } catch (IOException | InterruptedException e) {
//                StartupLevel.log.error("Error while starting network thread", e);
//            }
            StartupLevel.log.info("Server loaded in {} millisecond(s).", ServerInfoUtils.formatNumber(ManagementFactory.getRuntimeMXBean().getUptime()));
        }
    };

    private static final Logger log = LoggerFactory.getLogger(StartupLevel.class);
}

