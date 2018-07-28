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

package com.msemu.commons.reload;

import com.msemu.commons.utils.ClassUtils;
import com.msemu.core.startup.StartupComponent;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/14.
 */
@StartupComponent("Service")
public class ReloadService {
    private static final Logger log = LoggerFactory.getLogger(ReloadService.class);
    private static final AtomicReference<ReloadService> instance = new AtomicReference<>();
    private List<Class<? extends IReloadable>> classes = new ArrayList<>();
    private TreeMap<String, TreeMap<String, Class<? extends IReloadable>>> classTree = new TreeMap<>();

    private ReloadService() {
        ClassIndex.getAnnotated(Reloadable.class).forEach((item) -> {
            Reloadable annotation = item.getAnnotation(Reloadable.class);
            if (annotation != null) {
                String name = annotation.name();
                String group = annotation.group();
                this.classTree.computeIfAbsent(group, (m) -> new TreeMap<>())
                        .putIfAbsent(name, (Class<? extends IReloadable>) item);
            }
        });
        log.info("Loaded {} reloadable instances.", this.classes.size());
    }

    public static ReloadService getInstance() {
        ReloadService value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new ReloadService();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    private void callReload(Class<? extends IReloadable> clazz) {
        IReloadable object = (IReloadable) ClassUtils.singletonInstance(clazz);
        if (object != null) {
            object.reload();
        }
    }

    public int reloadByName(String name) {
        int count = 0;
        Collection<Class<? extends IReloadable>> classes = this.classTree.values().stream().filter((item) -> item.containsKey(name)).flatMap((item) -> item.values().stream()).collect(Collectors.toList());
        if (!classes.isEmpty()) {
            classes.forEach(this::callReload);
        }
        return count;
    }

    public int reloadByGroup(String group) {
        int count = 0;
        if (this.classTree.containsKey(group)) {
            Collection<Class<? extends IReloadable>> classes = this.classTree.get(group).values();
            classes.forEach(this::callReload);
        }
        return count;
    }
}
