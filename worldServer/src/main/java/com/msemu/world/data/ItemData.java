package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.EquipTemplateDatLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.loader.dat.SetItemInfoDatLoader;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.data.templates.SetItemInfo;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.ItemConstants;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
 */
@Reloadable(name = "item", group = "all")
@StartupComponent("Data")
public class ItemData implements IReloadable {

    private static final Logger log = LoggerFactory.getLogger(ItemData.class);
    private static final AtomicReference<ItemData> instance = new AtomicReference<>();

    @Getter(AccessLevel.PRIVATE)
    private static final ItemOptionDatLoader itemOptionsLoader = new ItemOptionDatLoader();
    @Getter(AccessLevel.PRIVATE)
    private static final ItemTemplateDatLoader itemTemplateDatLoader = new ItemTemplateDatLoader();
    @Getter(AccessLevel.PRIVATE)
    private static final EquipTemplateDatLoader equipTemplateDatLoader = new EquipTemplateDatLoader();
    @Getter(AccessLevel.PRIVATE)
    private static final SetItemInfoDatLoader setItemInfoDatLoader = new SetItemInfoDatLoader();


    public ItemData() {
        load();
    }

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

    public void load() {
        getItemTemplateDatLoader().load();
        log.info("{} Items loaded.", getItemTemplateDatLoader().size());
        getEquipTemplateDatLoader().load();
        log.info("{} Equips loaded.", getEquipTemplateDatLoader().size());
        getItemOptionsLoader().load();
        log.info("{} ItemOptions loaded.", getItemOptionsLoader().getData().size());
        getSetItemInfoDatLoader().load();
        log.info("{} SetItemInfo loaded.", getSetItemInfoDatLoader().getData().size());
    }

    private void clear() {
        getItemOptionsLoader().getData().clear();
        getItemTemplateDatLoader().getData().clear();
        getEquipTemplateDatLoader().getData().clear();
        getSetItemInfoDatLoader().getData().clear();
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
        EquipTemplate template = getEquipTemplateDatLoader().getItem(itemId);
        if (template == null)
            return null;
        Equip equip = new Equip(template);
        return equip;
    }


    public Item getItemFromTemplate(int itemId) {
        ItemTemplate template = getItemTemplateDatLoader().getItem(itemId);
        if (template == null)
            return null;
        Item item = new Item(template);
        return item;
    }

    public Item createItem(int itemId) {
        Item item;
        if (ItemConstants.isEquip(itemId)) {
            item = ItemData.getInstance().getEquipFromTemplate(itemId);
        } else {
            item = ItemData.getInstance().getItemFromTemplate(itemId);
            item.setQuantity(itemId);
        }
        return item;
    }

    public ItemTemplate getItemInfo(int itemId) {
        return getItemTemplateDatLoader().getItem(itemId);
    }

    public EquipTemplate getEquipInfo(int itemId) {
        return getEquipTemplateDatLoader().getItem(itemId);
    }

    public SetItemInfo getSetItemInfo(int setItemID) {
        return getSetItemInfoDatLoader().getItem(setItemID);
    }

}
