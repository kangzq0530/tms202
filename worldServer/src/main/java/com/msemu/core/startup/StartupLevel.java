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

