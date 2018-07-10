package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.MobTemplateDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.MobTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.quest.req.QuestProgressMobRequirement;
import com.msemu.world.client.field.lifes.Mob;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "mob", group = "all")
@StartupComponent("Data")
public class MobData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(MobData.class);
    private static final AtomicReference<MobData> instance = new AtomicReference<>();
    @Getter
    private final MobTemplateDatLoader mobTemplateDatLoader = new MobTemplateDatLoader();


    public MobData() {
        load();
    }

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

    public void load() {
        WzManager wzManager = WorldWzManager.getInstance();
        getMobTemplateDatLoader().load();
        linkMobAndQuest();
        log.info("{} MobTemplates laoded", getMobTemplateDatLoader().getData().size());
    }

    public void clear() {
        getMobTemplateDatLoader().getData().clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Mob getMobFromTemplate(int templateId) {
        DropData dropData = DropData.getInstance();
        MobTemplate mt = getMobTemplateDatLoader().getItem(templateId);
        if (mt == null)
            return null;
        Mob mob = new Mob(-1, mt);
        mob.setDropsInfo(dropData.getDropsInfoByMobID(templateId));
        mob.init();
        return mob;
    }

    public MobTemplate getMobTemplate(int templateId) {
        return getMobTemplateDatLoader().getItem(templateId);
    }

    public void linkMobAndQuest() {
        QuestData.getInstance().getQuestsProgressRequirements()
                .forEach((questID, reqs) -> {
                    reqs.stream().filter(req -> req instanceof QuestProgressMobRequirement)
                            .map(req -> (QuestProgressMobRequirement) req)
                            .forEach(req -> {
                                MobTemplate mt = getMobTemplate(req.getMobID());
                                if (mt != null) {
                                    mt.addLinkedQuest(questID);
                                }
                            });
                });
    }
}