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

package com.msemu.world.client.enchant.singleui;

import com.msemu.commons.data.enums.EnchantStat;
import com.msemu.commons.data.enums.EquipSpecialAttribute;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_EquipmentEnchantDisplay;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.InventoryManipulator;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.enchant.singleui.action.DisplayScrollUpgradeAction;
import com.msemu.world.client.enchant.singleui.action.ScrollUpgradeResultAction;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.enums.ScrollIcon;
import com.msemu.world.enums.UpgradeScrollResultType;
import com.msemu.world.enums.UpgradeScrollType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UpgradeScrollTabHandler {

    @Getter
    private List<ScrollUpgradeInfo> upgradeScrolls = new ArrayList<>();

    private static final AtomicReference<UpgradeScrollTabHandler> instance = new AtomicReference<>();

    public static UpgradeScrollTabHandler getInstance() {
        UpgradeScrollTabHandler value = instance.get();
        if (value == null) {
            synchronized (UpgradeScrollTabHandler.instance) {
                value = instance.get();
                if (value == null) {
                    value = new UpgradeScrollTabHandler();
                }
                instance.set(value);
            }
        }
        return value;
    }


    public UpgradeScrollTabHandler() {
        loadUpgradeScrolls();
    }

    private void loadUpgradeScrolls() {
        getUpgradeScrolls().clear();

        final EnchantStat[] stats = {EnchantStat.STR, EnchantStat.DEX, EnchantStat.INT, EnchantStat.LUK};
        final String[] names = {"力量卷軸", "敏捷卷軸", "智力卷軸", "幸運卷軸"};
        for (int i = 0; i < 4; i++) {
            ScrollUpgradeInfo p100 = new ScrollUpgradeInfo();
            p100.setIcon(ScrollIcon.ICON_PROP_100);
            p100.setType(UpgradeScrollType.INC_STATS);
            p100.addStat(stats[i], 1);
            p100.addStat(EnchantStat.MHP, 5);
            p100.addStat(EnchantStat.PDD, 1);
            p100.addStat(EnchantStat.MDD, 1);
            p100.setProp(100);
            p100.setCost(30);
            p100.setCostBefore(30);
            p100.setTitle("100% " + names[i]);

            ScrollUpgradeInfo p70 = new ScrollUpgradeInfo();
            p70.setIcon(ScrollIcon.ICON_PROP_70);
            p70.setType(UpgradeScrollType.INC_STATS);
            p70.addStat(stats[i], 2);
            p70.addStat(EnchantStat.MHP, 15);
            p70.addStat(EnchantStat.PDD, 2);
            p70.addStat(EnchantStat.MDD, 2);
            p70.setProp(70);
            p70.setCost(70);
            p70.setCostBefore(70);
            p70.setTitle("70% " + names[i]);

            ScrollUpgradeInfo p30 = new ScrollUpgradeInfo();
            p30.setIcon(ScrollIcon.ICON_PROP_30);
            p30.setType(UpgradeScrollType.INC_STATS);
            p30.addStat(stats[i], 3);
            p30.addStat(EnchantStat.MHP, 30);
            p30.addStat(EnchantStat.PDD, 4);
            p30.addStat(EnchantStat.MDD, 4);
            p30.setProp(30);
            p30.setCost(100);
            p30.setCostBefore(100);
            p30.setTitle("30% " + names[i]);


            ScrollUpgradeInfo p15 = new ScrollUpgradeInfo();
            p15.setIcon(ScrollIcon.ICON_PROP_15);
            p15.setType(UpgradeScrollType.INC_STATS);
            p15.addStat(stats[i], 4);
            p15.addStat(EnchantStat.MHP, 45);
            p15.addStat(EnchantStat.PDD, 8);
            p15.addStat(EnchantStat.MDD, 8);
            p15.setProp(15);
            p15.setCost(150);
            p15.setCostBefore(150);
            p15.setTitle("15% " + names[i]);

            getUpgradeScrolls().add(p100);
            getUpgradeScrolls().add(p70);
            getUpgradeScrolls().add(p30);
            getUpgradeScrolls().add(p15);
        }

    }

    public void onDisplay(Character chr, int bagIndex) {

        final Inventory inventory = bagIndex < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
        inventory.doLock(() -> {

            final Item item = chr.getEquipInventory().getItemBySlot(bagIndex);

            //TODO 檢查能不能衝卷軸

            List<ScrollUpgradeInfo> scrolls = getUpgradeScrolls();
            chr.write(new LP_EquipmentEnchantDisplay(new DisplayScrollUpgradeAction(scrolls)));
        });
    }

    public void onUpgrade(Character chr, int scrollIndex, int bagIndex) {
        final Inventory equipInv = bagIndex < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
        equipInv.doLock(() -> {
            chr.getEtcInventory().doLock(() -> {
                //  衝捲
                final Equip equip = (Equip) equipInv.getItemBySlot(Math.abs(bagIndex));

                if (equip == null || scrollIndex >= getUpgradeScrolls().size()) {
                    return;
                }

                final ScrollUpgradeInfo scroll = getUpgradeScrolls().get(scrollIndex);
                final UpgradeScrollType scrollType = scroll.getType();
                final int VestigeCost = scroll.getCost();
                boolean hasVestige = equip.hasSpecialAttribute(EquipSpecialAttribute.VESTIGE);
                boolean hasItem = chr.hasItemCount(ItemConstants.ITEM_VESTIGE, VestigeCost);
                boolean canUpgrade = false;
                if (scrollType.equals(UpgradeScrollType.INC_STATS)) {
                    canUpgrade = equip.getRuc() > 0;
                } else if (scrollType.equals(UpgradeScrollType.INNOCENCE_SCROLL)) {

                } else if (scrollType.equals(UpgradeScrollType.CLEAN_STATS_SCROLL)) {

                }
                if (hasVestige || !hasItem || !canUpgrade) {
                    chr.enableActions();
                    return;
                }

                ScrollUpgradeInfo scrollInfo = getUpgradeScrolls().get(scrollIndex);

                boolean success = Rand.getChance(scroll.getProp());

                final Equip oldEquip = equip.deepCopy();

                equip.setRuc((short) (equip.getRuc() - 1));

                InventoryManipulator.consume(chr, ItemConstants.ITEM_VESTIGE, scrollInfo.getCost(), true);

                if (success) {
                    if (scrollType.equals(UpgradeScrollType.INC_STATS)) {
                        equip.setCuc((short) (equip.getCuc() + 1));
                        scrollInfo.getStats().forEach((stat, value) -> {
                            switch (stat) {
                                case PAD:
                                    equip.setIPad((short) (equip.getIPad() + value));
                                    break;
                                case MAD:
                                    equip.setIMad((short) (equip.getIMad() + value));
                                    break;
                                case STR:
                                    equip.setIStr((short) (equip.getIStr() + value));
                                    break;
                                case DEX:
                                    equip.setIDex((short) (equip.getIDex() + value));
                                    break;
                                case INT:
                                    equip.setIInt((short) (equip.getIInt() + value));
                                    break;
                                case PDD:
                                    equip.setIPDD((short) (equip.getIPDD() + value));
                                    break;
                                case MDD:
                                    equip.setIMDD((short) (equip.getIMDD() + value));
                                    break;
                                case JUMP:
                                    equip.setIJump((short) (equip.getIJump() + value));
                                    break;
                                case SPEED:
                                    equip.setISpeed((short) (equip.getISpeed() + value));
                                    break;
                                case MMP:
                                    equip.setIMaxMp((short) (equip.getIMaxMp() + value));
                                    break;
                                case MHP:
                                    equip.setIMaxHp((short)(equip.getIMaxHp() + value));
                                    break;
                            }
                        });
                    }
                }
                chr.write(new LP_EquipmentEnchantDisplay(new ScrollUpgradeResultAction(false, success ? UpgradeScrollResultType.SUCCESS : UpgradeScrollResultType.FAIL
                        , success ? "[成功]" : "[失敗]", oldEquip, equip)));
                InventoryManipulator.update(chr, equip);

            });
        });
    }

}
