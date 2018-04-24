package com.msemu.world.data;

import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.life.Npc;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
 */
@Reloadable(name = "npc", group = "all")
@StartupComponent("Data")
public class NpcData implements IReloadable {


    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private final Map<Integer, NpcTemplate> npcTemplates = new HashMap<>();

    private static final AtomicReference<NpcData> instance = new AtomicReference<>();


    public static NpcData getInstance() {
        NpcData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new NpcData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public NpcData() {
        load();
    }

    public void load() {
        log.info("{} NpcTemplate loaded", this.npcTemplates.size());
    }

    public void clear() {
        this.npcTemplates.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Npc getNpcFromTemplate(int templateId) {
        throw new NotImplementedException();
    }
}
