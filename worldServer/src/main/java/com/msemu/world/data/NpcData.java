package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.NpcTemplateDatLoader;
import com.msemu.commons.data.templates.NpcTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.lifes.Npc;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
 */
@Reloadable(name = "requestNpcs", group = "all")
@StartupComponent("Data")
public class NpcData implements IReloadable {


    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private final Set<NpcTemplate> npcTemplates = new HashSet<>();

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
        WzManager wzManager = WorldWzManager.getInstance();
        getNpcTemplates().addAll(new NpcTemplateDatLoader().load(null));
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
        NpcTemplate nt = getNpcTemplateFromId(templateId);
        Npc npc = new Npc(-1, nt);
        return npc;
    }

    private NpcTemplate getNpcTemplateFromId(int templateId) {
        return getNpcTemplates().stream().filter(nt->nt.getId()== templateId)
                .findFirst().orElse(null);
    }
}
