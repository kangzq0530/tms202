package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.EquipTemplateDatLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
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
        WorldWzManager wzManager = WorldWzManager.getInstance();
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


    /**
     * Creates a new Equip given an itemId.
     *
     * @param itemId The itemId of the wanted equip.
     * @return A deep copy of the default values of the corresponding Equip, or null if there is no equip with itemId
     * <code>itemId</code>.
     */
    public Equip getEquipFromTemplate(int itemId) {
        EquipTemplate template = getEquipTemplates().get(itemId);
        if (template == null)
            return null;
        Equip equip = new Equip(template);
        return equip;
    }


    public Item getItemFromTemplate(int itemId) {
        ItemTemplate template = getItemTemplates().get(itemId);
        if (template == null)
            return null;
        Item item = new Item(template);
        return item;
    }

    public ItemTemplate getItemInfo(int itemId) {
        return getItemTemplates().get(itemId);
    }

}
