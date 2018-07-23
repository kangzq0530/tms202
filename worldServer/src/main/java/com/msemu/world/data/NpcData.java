package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.NpcTemplateDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.lifes.Npc;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
 */
@Reloadable(name = "requestNPC", group = "all")
@StartupComponent("Data")
public class NpcData implements IReloadable {


    private static final Logger log = LoggerFactory.getLogger(QuestData.class);
    private static final AtomicReference<NpcData> instance = new AtomicReference<>();
    @Getter
    private final NpcTemplateDatLoader npcTemplateDatLoader = new NpcTemplateDatLoader();


    public NpcData() {
        load();
    }

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

    public void load() {
        WzManager wzManager = WorldWzManager.getInstance();
        getNpcTemplateDatLoader().load();
        log.info("{} NpcTemplate loaded", getNpcTemplateDatLoader().getData().size());
    }

    public void clear() {
        getNpcTemplateDatLoader().getData().clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Npc getNpcFromTemplate(int templateId) {
        NpcTemplate nt = getNpcTemplateFromId(templateId);
        Npc npc = new Npc(-1, nt);
        return npc;
    }

    private NpcTemplate getNpcTemplateFromId(int templateId) {
        return getNpcTemplateDatLoader().getData().stream().filter(nt -> nt.getId() == templateId)
                .findFirst().orElse(null);
    }
}
