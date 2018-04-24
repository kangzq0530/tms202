package com.msemu.world.data;

import com.msemu.commons.data.loader.wz.FieldTemplateLoader;
import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.wz.WzManager;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.life.DropInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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
        FieldTemplate template = getFieldTemplates().get(templateId);
        Field field = new Field(templateId, template);
        field.init();
        return field;
    }
}