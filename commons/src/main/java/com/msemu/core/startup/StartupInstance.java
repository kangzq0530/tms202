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