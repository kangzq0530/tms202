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

package com.msemu;

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
