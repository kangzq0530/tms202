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

import com.msemu.commons.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/3/14.
 */
public class StartModule<SL extends Enum<SL>> {
    private final List<StartModule<SL>> dependency = new ArrayList<>();
    private final SL startLevel;
    private final Class<?> clazz;
    private Object instance;

    public StartModule(SL startLevel, Class<?> clazz) {
        this.startLevel = startLevel;
        this.clazz = clazz;
    }

    public void addDependency(StartModule<SL> module) {
        this.dependency.add(module);
    }

    public void init() {
        if (this.instance == null) {
            this.dependency.forEach(StartModule::init);
            this.instance = ClassUtils.singletonInstance(this.clazz);
        }
    }

    public SL getStartLevel() {
        return this.startLevel;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public Object getInstance() {
        return this.instance;
    }
}