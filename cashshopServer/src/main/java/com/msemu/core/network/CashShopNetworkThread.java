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

@StartupComponent("Network")
public class CashShopNetworkThread extends NetworkThread<ShopClient> {

    private static final AtomicReference<CashShopNetworkThread> instance = new AtomicReference<>();

    public CashShopNetworkThread() {
        super(NetworkConfig.HOST, NetworkConfig.PORT);
    }

    public static CashShopNetworkThread getInstance() {
        CashShopNetworkThread value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new CashShopNetworkThread();
                }
                instance.set(value);
            }
        }
        return instance.get();
    }

    @Override
    public IClientFactory<ShopClient> getClientFactory() {
        return null;
    }

    @Override
    public AbstractPacketHandlerFactory<ShopClient> getPacketHandler() {
        return null;
    }
}
