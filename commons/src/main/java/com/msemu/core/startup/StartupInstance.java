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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Weber on 2018/3/14.
 */
public class StartupInstance<SL extends Enum<SL>> {
    private static final Logger log = LoggerFactory.getLogger(StartupInstance.class);
    private final HashMap<SL, List<StartModule<SL>>> startTable = new HashMap<>();

    public StartupInstance() {
    }

    void put(SL level, StartModule<SL> module) {
        List<StartModule<SL>> invokes = this.startTable.computeIfAbsent(level, (k) -> new ArrayList());
        invokes.add(module);
    }

    protected Collection<Map.Entry<SL, List<StartModule<SL>>>> getAll() {
        return this.startTable.entrySet();
    }

    void runLevel(SL level) {
        List<StartModule<SL>> list = this.startTable.get(level);
        if (list != null) {
            list.forEach(StartModule::init);
        }
    }
}