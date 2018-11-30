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

import com.msemu.commons.network.IClientFactory;
import com.msemu.commons.network.NetworkThread;
import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/19.
 */
@StartupComponent("Network")
public class LoginNetworkThread extends NetworkThread<LoginClient> {

    private static final AtomicReference<LoginNetworkThread> instance = new AtomicReference<>();

    protected LoginNetworkThread() {
        super(NetworkConfig.HOST, NetworkConfig.PORT);
    }

    public static LoginNetworkThread getInstance() {
        LoginNetworkThread value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginNetworkThread();
                }
                instance.set(value);
            }
        }
        return instance.get();
    }

    @Override
    public IClientFactory<LoginClient> getClientFactory() {
        return LoginClientFactory.getInstance();
    }

    @Override
    public AbstractPacketHandlerFactory<LoginClient> getPacketHandler() {
        return LoginPacketFactory.getInstance();
    }
}
