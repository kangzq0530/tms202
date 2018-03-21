package com.msemu.world.dataholders;

import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.ServerInfoUtils;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/21.
 */
@Reloadable(name = "skill", group = "wz")
@StartupComponent("Data")
public class SkillData implements IReloadable{

    private static final AtomicReference<SkillData> instance = new AtomicReference<>();


    private SkillData() {
        load();
    }

    public static SkillData getInstance() {
        SkillData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new SkillData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    private void load() {
        ServerInfoUtils.printSection("PetsData Loading");

    }

    @Override
    public void reload() {
        load();
    }
}
