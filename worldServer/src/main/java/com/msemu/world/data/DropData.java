package com.msemu.world.data;

import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.life.DropInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/13.
 */
@Reloadable(name = "drop", group = "all")
@StartupComponent("Data")
public class DropData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private final Map<Integer, DropInfo> dropsInfo = new HashMap<>();

    private static final AtomicReference<DropData> instance = new AtomicReference<>();


    public static DropData getInstance() {
        DropData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new DropData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public DropData() {
        load();
    }

    public void load() {
        log.info("{} drops laoded", this.dropsInfo.size());
    }

    public void clear() {
        this.dropsInfo.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }
}