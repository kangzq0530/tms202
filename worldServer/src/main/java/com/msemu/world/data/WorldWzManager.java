package com.msemu.world.data;

import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/22.
 */
@StartupComponent("Wz")
public class WorldWzManager extends WzManager {

    private static final AtomicReference<WorldWzManager> instance = new AtomicReference<>();


    public WorldWzManager() {
        super();
        //super(WzManager.ETC, WzManager.ITEM, WzManager.CHARACTER, WzManager.STRING, WzManager.NPC, WzManager.MAP, WzManager.QUEST, WzManager.SKILL);
    }

    public static WorldWzManager getInstance() {
        WorldWzManager value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new WorldWzManager();
                    instance.set(value);
                }
            }
        }
        return value;
    }

}

