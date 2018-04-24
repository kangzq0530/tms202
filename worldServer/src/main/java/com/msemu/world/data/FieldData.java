package com.msemu.world.data;

import com.msemu.commons.data.loader.wz.FieldTemplateLoader;
import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.wz.WzManager;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.Npc;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "field", group = "all")
@StartupComponent("Data")
public class FieldData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private static final HashMap<Integer, FieldTemplate> fieldTemplates = new HashMap<>();

    private static final AtomicReference<FieldData> instance = new AtomicReference<>();


    public static FieldData getInstance() {
        FieldData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new FieldData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public FieldData() {
        load();
    }

    public void load() {
        WzManager wzManager = new WorldWzManager();
        getFieldTemplates().putAll(new FieldTemplateLoader().load(wzManager));
        log.info("{} fieldTemplates loaded", getFieldTemplates().size());
    }

    public void clear() {
        getFieldTemplates().clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Field getFieldFromTemplate(int templateId) {
        FieldTemplate fieldTemplate = getFieldTemplates().get(templateId);
        Field field = new Field(templateId, fieldTemplate);
        field.getFieldData().getLife().forEach(lifeData -> {
            if (lifeData.getType().equalsIgnoreCase("n")) {
                Npc npc = NpcData.getInstance().getNpcFromTemplate(lifeData.getId());
                npc.setLifeType(lifeData.getType());
                npc.setX(lifeData.getX());
                npc.setY(lifeData.getY());
                npc.setCy(lifeData.getCy());
                npc.setRx0(lifeData.getRx0());
                npc.setRx1(lifeData.getRx1());
                npc.setF(lifeData.getF());
                npc.setFh(lifeData.getFh());
                npc.setLimitedName(lifeData.getLimitedname());
                npc.setUseDay(lifeData.isUseDay());
                npc.setUseNight(lifeData.isUseNight());
                npc.setHold(lifeData.isHold());
                npc.setNoFoothold(lifeData.isNofoothold());
                npc.setDummy(lifeData.isDummy());
                npc.setSpine(lifeData.isSpine());
                npc.setMobTimeOnDie(lifeData.isMobTimeOnDie());
                npc.setMobAliveReq(lifeData.getMobAliveReq());
                npc.setRegenStart(lifeData.getRegenStart());
                npc.setPosition(new Position(npc.getX(), npc.getY()));
                field.addLife(npc);
            } else if (lifeData.getType().equalsIgnoreCase("m")) {
                Mob mob = MobData.getInstance().getNpcFromTemplate(lifeData.getId());
                mob.setLifeType(lifeData.getType());
                mob.setX(lifeData.getX());
                mob.setY(lifeData.getY());
                mob.setCy(lifeData.getCy());
                mob.setRx0(lifeData.getRx0());
                mob.setRx1(lifeData.getRx1());
                mob.setF(lifeData.getF());
                mob.setFh(lifeData.getFh());
                mob.setLimitedName(lifeData.getLimitedname());
                mob.setUseDay(lifeData.isUseDay());
                mob.setUseNight(lifeData.isUseNight());
                mob.setHold(lifeData.isHold());
                mob.setNoFoothold(lifeData.isNofoothold());
                mob.setDummy(lifeData.isDummy());
                mob.setSpine(lifeData.isSpine());
                mob.setMobTimeOnDie(lifeData.isMobTimeOnDie());
                mob.setMobAliveReq(lifeData.getMobAliveReq());
                mob.setRegenStart(lifeData.getRegenStart());
                mob.setMobTime(lifeData.getMobTime());
                mob.setHide(lifeData.isHide());
                mob.setPosition(new Position(mob.getX(), mob.getY()));
                mob.setHomePosition(new Position(mob.getX(), mob.getY()));
            }
        });
        return field;
    }
}