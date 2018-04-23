package com.msemu.world.data;

import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.life.Mob;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "mob", group = "all")
@StartupComponent("Data")
public class MobData implements IReloadable{
    private static final Logger log = LoggerFactory.getLogger(MobData.class);
    @Getter
    private final Map<Integer, MobTemplate> mobTemplates = new HashMap<>();

    private static final AtomicReference<MobData> instance = new AtomicReference<>();


    public static MobData getInstance() {
        MobData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new MobData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public MobData() {
        load();
    }

    public void load() {
        log.info("{} MobTemplates laoded", this.mobTemplates.size());
    }

    public void clear() {
        this.mobTemplates.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Mob createMobFromTemplate(int templateId) {
        throw new NotImplementedException();
    }
}