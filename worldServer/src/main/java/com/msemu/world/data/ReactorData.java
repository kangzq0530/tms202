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

package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.ReactorTemplateDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.field.ReactorTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Reloadable(name = "reactor", group = "all")
@StartupComponent("Data")
public class ReactorData implements IReloadable {

    private static final Logger log = LoggerFactory.getLogger(ReactorData.class);

    private static final AtomicReference<ReactorData> instance = new AtomicReference<>();

    @Getter(AccessLevel.PRIVATE)
    private ReactorTemplateDatLoader loader = new ReactorTemplateDatLoader();

    public static ReactorData getInstance() {
        ReactorData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new ReactorData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public ReactorData() {
        load();
    }

    public ReactorTemplate getReactorTemplateById(int templateId) {
        return getLoader().getData().getOrDefault(templateId, null);
    }

    private void load() {
        getLoader().load();
        log.info("{} ReactorTemplate loaded", getLoader().getData().size());
    }

    private void clear() {
        getLoader().getData().clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }
}
