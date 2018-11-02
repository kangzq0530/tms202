/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.data;

import com.msemu.commons.data.enums.EquipBaseStat;
import com.msemu.commons.data.enums.ItemGrade;
import com.msemu.commons.data.loader.dat.EquipTemplateDatLoader;
import com.msemu.commons.data.loader.dat.ItemOptionDatLoader;
import com.msemu.commons.data.loader.dat.ItemTemplateDatLoader;
import com.msemu.commons.data.loader.dat.SetItemInfoDatLoader;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.data.templates.ItemOptionInfo;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.data.templates.SetItemInfo;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.ItemConstants;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
    private Equip getEquipFromTemplate(int itemId) {
        EquipTemplate template = getEquipTemplateDatLoader().getItem(itemId);
        if (template == null)
            return null;
        Equip equip = new Equip(template);
        return equip;
    }


    private Item getItemFromTemplate(int itemId) {
        ItemTemplate template = getItemTemplateDatLoader().getItem(itemId);
        if (template == null)
            return null;
        Item item = new Item(template);
        return item;
    }

    public Item createItem(int itemId) {
        return createItem(itemId, false);
    }

    public Item createItem(int itemId, boolean randomStats) {
        Item item;
        if (ItemConstants.isEquip(itemId)) {
            item = ItemData.getInstance().getEquipFromTemplate(itemId);
            if (item != null) {
                EquipBaseStat[] ebsStat = new EquipBaseStat[]{EquipBaseStat.iStr, EquipBaseStat.iInt, EquipBaseStat.iDex,
                        EquipBaseStat.iLuk, EquipBaseStat.iPAD, EquipBaseStat.iMAD, EquipBaseStat.iMaxHP, EquipBaseStat.iMaxMP,
                        EquipBaseStat.iPAD, EquipBaseStat.iMAD};
                Equip equip = (Equip) item;
                for (EquipBaseStat ebs : ebsStat) {
                    int max = ebs == EquipBaseStat.iPAD || ebs == EquipBaseStat.iMAD ? 5 : 3; // Att +-5, the rest +-3
                    if (equip.getBaseStat(ebs) > 0) {
                        int rand = Rand.get(max);
                        rand = Rand.nextBoolean() ? rand : -rand;
                        int newStat = (int) Math.max(0, equip.getBaseStat(ebs) + rand);
                        equip.setBaseStat(ebs, newStat);
                    }
                }
                ItemGrade grade = ItemGrade.NONE;
                if (Rand.getChance(GameConstants.RANDOM_EQUIP_UNIQUE_CHANCE)) {
                    grade = ItemGrade.HIDDEN_UNIQUE;
                } else if (Rand.getChance(GameConstants.RANDOM_EQUIP_EPIC_CHANCE)) {
                    grade = ItemGrade.HIDDEN_EPIC;
                } else if (Rand.getChance(GameConstants.RANDOM_EQUIP_RARE_CHANCE)) {
                    grade = ItemGrade.HIDDEN_RARE;
                }
                if (grade != ItemGrade.NONE) {
                    equip.setHiddenOptionBase(grade.getValue(), GameConstants.THIRD_LINE_CHANCE);
                }
            }
        } else {
            item = ItemData.getInstance().getItemFromTemplate(itemId);
            if (item != null) {
                item.setQuantity(1);
                item.setDateExpire(FileTime.getFileTimeFromType(FileTime.Type.MAX_TIME));
            }
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


    public List<ItemOptionInfo> getAllItemOptionInfos() {
        return new ArrayList<>(getItemOptionsLoader().getData().values());
    }
}
