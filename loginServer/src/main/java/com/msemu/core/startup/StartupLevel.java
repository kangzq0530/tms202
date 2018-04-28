package com.msemu.core.startup;

import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.commons.utils.versioning.Version;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.network.LoginNetworkThread;
import com.msemu.login.data.LoginWzManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created by Weber on 2018/3/14.
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
            LoginWzManager.getInstance().clearData();
            System.gc();
            System.runFinalization();
            for (final String line : ServerInfoUtils.getMemUsage()) {
                StartupLevel.log.info(line);
            }
            try {
                LoginNetworkThread.getInstance().startup();
                log.info("登入伺服器等待連接於 {}:{}", NetworkConfig.HOST, NetworkConfig.PORT);
            } catch (IOException | InterruptedException e) {
                StartupLevel.log.error("Error while starting network thread", e);
            }
            StartupLevel.log.info("登入伺服器於 {} 毫秒讀取完畢.", ServerInfoUtils.formatNumber(ManagementFactory.getRuntimeMXBean().getUptime()));
        }
    };

    private static final Logger log = LoggerFactory.getLogger(StartupLevel.class);
}