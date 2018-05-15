package com.msemu.core.startup;

import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.commons.utils.versioning.Version;
import com.msemu.core.network.WorldNetworkThread;
import com.msemu.core.tools.scriptmessage.ScriptMessageTester;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    Wz,
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

            try {
                WorldNetworkThread.getInstance().startup();
            } catch (IOException | InterruptedException e) {
                StartupLevel.log.error("Error while starting network thread", e);
            }
            StartupLevel.log.info("Server loaded in {} millisecond(s).", ServerInfoUtils.formatNumber(ManagementFactory.getRuntimeMXBean().getUptime()));
            Application.launch(ScriptMessageTester.class);
        }
    };

    private static final Logger log = LoggerFactory.getLogger(StartupLevel.class);
}

