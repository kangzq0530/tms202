package com.msemu.commons.reload;

import com.msemu.commons.utils.ClassUtils;
import com.msemu.core.startup.StartupComponent;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
            for (Iterator<Class<? extends IReloadable>> var4 = classes.iterator(); var4.hasNext(); ++count) {
                Class<? extends IReloadable> clazz = var4.next();
                this.callReload(clazz);
            }
        }
        return count;
    }

    public int reloadByGroup(String group) {
        int count = 0;
        if (this.classTree.containsKey(group)) {
            Collection<Class<? extends IReloadable>> classes = this.classTree.get(group).values();
            for (Iterator<Class<? extends IReloadable>> it = classes.iterator(); it.hasNext(); ++count) {
                Class<? extends IReloadable> clazz = it.next();
                this.callReload(clazz);
            }
        }
        return count;
    }
}
