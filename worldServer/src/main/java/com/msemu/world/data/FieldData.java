package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.FieldTemplateDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.FieldObject;
import com.msemu.world.client.field.spawns.MobSpawnPoint;
import com.msemu.world.client.field.spawns.NpcSpawnPoint;
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
    private static final AtomicReference<FieldData> instance = new AtomicReference<>();
    @Getter
    private final HashMap<Integer, FieldTemplate> fieldTemplates = new HashMap<>();


    public FieldData() {
        load();
    }

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

    public void load() {
        WzManager wzManager = new WorldWzManager();
        getFieldTemplates().putAll(new FieldTemplateDatLoader().load(null));
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
        if (fieldTemplate == null)
            return null;
        Field field = new Field(-1, fieldTemplate);
        fieldTemplate.getLife().forEach(lifeData -> {
            if (lifeData.getType().equals("m")) {
                field.addSpawnPoint(new MobSpawnPoint(lifeData));
            } else {
                field.addSpawnPoint(new NpcSpawnPoint(lifeData));
            }
        });
        field.getFieldData().getObjects().forEach(fieldObjectInfo -> {
            field.getObjects().add(new FieldObject(fieldObjectInfo));
        });
        return field;
    }
}