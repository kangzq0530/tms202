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