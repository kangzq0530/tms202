package com.msemu.login.data;

import com.msemu.commons.data.loader.dat.EquipTemplateDatLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.login.client.character.items.Equip;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/21.
 */
@Reloadable(name = "item", group = "all")
@StartupComponent("Data")
public class ItemData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(ItemData.class);

    @Getter(value = AccessLevel.PRIVATE)
    private final Map<Integer, ItemOptionInfo> itemOptions = new HashMap<>();

    @Getter(value = AccessLevel.PRIVATE)
    private final Map<Integer, EquipTemplate> equipTemplates = new HashMap<>();

    @Getter(value = AccessLevel.PRIVATE)
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
        itemOptions.putAll(new ItemOptionDatLoader().load(null));
        log.info("{} ItemOptions loaded.", itemOptions.size());
        itemTemplates.putAll(new ItemTemplateDatLoader().load(null));
        log.info("{} ItemTemplate loaded.", itemTemplates.size());
        equipTemplates.putAll(new EquipTemplateDatLoader().load(null));
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


    public Equip getEquipFromTemplate(int itemID) {
        EquipTemplate template = getEquipTemplates().get(itemID);
        if (template == null)
            return null;
        Equip equip = new Equip(template);
        return equip;
    }

}
