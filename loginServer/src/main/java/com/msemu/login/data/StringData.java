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

package com.msemu.login.data;

import com.msemu.commons.data.loader.dat.ForbiddenNameDatLoader;
import com.msemu.commons.data.templates.ForbiddenName;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/21.
 */
@Reloadable(name = "string", group = "all")
@StartupComponent("Data")
public class StringData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(StringData.class);
    private static final AtomicReference<StringData> instance = new AtomicReference<>();
    @Getter
    private ForbiddenNameDatLoader forbiddenNameDatLoader = new ForbiddenNameDatLoader();


    public StringData() {
        reload();
    }

    public static StringData getInstance() {
        StringData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new StringData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    private void clear() {
        //TODO
    }

    private void load() {

        getForbiddenNameDatLoader().load();
        log.info("{} forbiddenName loaded.", getForbiddenNameDatLoader().getData().getNames().size());
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public ForbiddenName getForbiddenName() {
        return getForbiddenNameDatLoader().getData();
    }
}
