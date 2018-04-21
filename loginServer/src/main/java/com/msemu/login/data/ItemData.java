package com.msemu.login.data;

import com.msemu.commons.data.loader.templates.EquipTemplate;
import com.msemu.commons.data.loader.wz.EquipTemplateLoader;
import com.msemu.commons.data.loader.wz.ItemOptionLoader;
import com.msemu.commons.data.loader.templates.ItemOption;
import com.msemu.commons.data.loader.templates.ItemTemplate;
import com.msemu.commons.data.loader.wz.ItemTemplateLoader;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/21.
 */
@Reloadable(name = "item", group = "all")
@StartupComponent("Data")
public class ItemData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(ItemData.class);

    private final Map<Integer, List<ItemOption>> itemOptions = new HashMap<>();

    private final Map<Integer, EquipTemplate> equipTemplates = new HashMap<>();

    private final Map<Integer, ItemTemplate> itemTemplates = new HashMap<>();

    private static final AtomicReference<ItemData> instance = new AtomicReference<>();


    public static ItemData getInstance() {
        ItemData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new ItemData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public ItemData() {
        load();
    }

    public void load() {
        LoginWzManager wzManager = LoginWzManager.getInstance();
        itemOptions.putAll(new ItemOptionLoader().load(wzManager));
        log.info("{} ItemOptions loaded.", itemOptions.size());
        itemTemplates.putAll(new ItemTemplateLoader().load(wzManager));
        log.info("{} ItemTemplate loaded.", itemTemplates.size());
        equipTemplates.putAll(new EquipTemplateLoader().load(wzManager));
        log.info("{} EquipTemplate loaded.", equipTemplates.size());
        System.gc();
    }

    private void clear() {
        itemOptions.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }


}
