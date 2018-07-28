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

package com.msemu.login.service;

import com.msemu.core.startup.StartupComponent;
import com.msemu.login.modules.FirewallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/15.
 */
@StartupComponent("Service")
public class FirewallService {
    private static final Logger log = LoggerFactory.getLogger((Class) FirewallService.class);
    private static final AtomicReference<FirewallService> instance = new AtomicReference<FirewallService>();
    private static FirewallFilter firewallFilter = new FirewallFilter();

    public static FirewallService getInstance() {
        FirewallService value = instance.get();
        if (value == null) {
            synchronized (FirewallService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new FirewallService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public static FirewallFilter getFirewallFilter() {
        return FirewallService.firewallFilter;
    }

}