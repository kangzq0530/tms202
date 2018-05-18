package com.msemu.core.startup;

import com.msemu.commons.utils.ServerInfoUtils;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */
public class StartupManager {
    private static final Logger log = LoggerFactory.getLogger(StartupManager.class);
    private static final AtomicReference<StartupManager> instance = new AtomicReference<>();

    public StartupManager() {
    }

    public static StartupManager getInstance() {
        StartupManager value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    StartupManager actualValue = new StartupManager();
                    value = actualValue;
                    instance.set(actualValue);
                }
            }
        }
        return value;
    }

    public <SL extends Enum<SL>> void startup(Class<SL> sl) {
        StartupInstance<SL> startup = new StartupInstance<>();
        Iterator<?> components = ClassIndex.getAnnotated(StartupComponent.class).iterator();
        while (components.hasNext()) {
            Class entry = (Class) components.next();
            StartupComponent invalidModules = (StartupComponent) entry.getAnnotation(StartupComponent.class);
            try {
                SL startupLevel = Enum.valueOf(sl, invalidModules.value());
                StartModule<SL> module = new StartModule<>(startupLevel, entry);
                startup.put(startupLevel, module);
            } catch (Exception except) {
                log.error("Error while initializing StartupManager", except);
            }
        }

        components = startup.getAll().iterator();

        while (components.hasNext()) {
            Map.Entry<SL, List<StartModule<SL>>> entry = (Map.Entry<SL, List<StartModule<SL>>>) components.next();
            List<StartModule<SL>> notfoundModules = new ArrayList<>();
            List<StartModule<SL>> modules = entry.getValue();
            for (StartModule<SL> module : modules) {
                Class clazz = module.getClazz();
                Class<? extends Object>[] dependency = ((StartupComponent) clazz.getAnnotation(StartupComponent.class)).dependency();
                for (Class<? extends Object> dep : dependency) {
                    Optional<StartModule<SL>> dependencyModule = modules.stream().filter((m) -> m.getClazz().getCanonicalName().equals(dep.getCanonicalName())).findAny();
                    if (dependencyModule.isPresent()) {
                        module.addDependency(dependencyModule.get());
                    } else {
                        notfoundModules.add(module);
                        log.warn("Not found dependency ({}) for {} on {} start maxLevel.", dep.getCanonicalName(), clazz.getCanonicalName(), module.getStartLevel().name());
                    }
                }
            }

            modules.removeAll(notfoundModules);
        }

        for (SL level : sl.getEnumConstants()) {
            ServerInfoUtils.printSection(level.name());
            ((IStartupLevel) level).invokeDepends();
            startup.runLevel(level);
        }

    }
}
