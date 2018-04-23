package com.msemu.world.data;

import com.msemu.commons.data.FieldTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.life.DropInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/11.
 */
@Reloadable(name = "field", group = "all")
@StartupComponent("Data")
public class FieldData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    private static List<FieldTemplate> fields = new ArrayList<>();

    @Getter
    private final Map<Integer, DropInfo> fieldTemplates = new HashMap<>();

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
        log.info("{} fieldTemplates laoded", this.fieldTemplates.size());
    }

    public void clear() {
        this.fieldTemplates.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public Field getFieldFromTemplate(int id) {
        throw new NotImplementedException();
    }
}