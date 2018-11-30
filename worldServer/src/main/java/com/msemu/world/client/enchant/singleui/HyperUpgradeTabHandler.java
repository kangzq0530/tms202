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

import com.msemu.commons.data.enums.EquipSpecialAttribute;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_EquipmentEnchantDisplay;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.enchant.singleui.action.DisplayHyperMiniGameAction;
import com.msemu.world.client.enchant.singleui.action.DisplayHyperUpgradeAction;
import com.msemu.world.constants.HyperUpgradeConstants;

import java.util.concurrent.atomic.AtomicReference;

public class HyperUpgradeTabHandler {

    private static final AtomicReference<HyperUpgradeTabHandler> instance = new AtomicReference<>();

    public static HyperUpgradeTabHandler getInstance() {
        HyperUpgradeTabHandler value = instance.get();
        if (value == null) {
            synchronized (HyperUpgradeTabHandler.instance) {
                value = instance.get();
                if (value == null) {
                    value = new HyperUpgradeTabHandler();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public void onMiniGame(final Character chr) {
        final int seed = Rand.nextInt();
        chr.setHyperMiniGameSeed(seed);
        chr.write(new LP_EquipmentEnchantDisplay(new DisplayHyperMiniGameAction(HyperUpgradeConstants.MINIGAME_LEVEL, chr.getHyperMiniGameSeed())));
    }

    public void onDisplay(final Character chr, int bagIndex, boolean safeUpgrade) {
        Inventory inventory = bagIndex < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
        inventory.doLock(() -> {
            final Equip equip = (Equip) inventory.getItemBySlot(bagIndex);
            final EquipTemplate template = equip.getTemplate();

            if (equip.hasSpecialAttribute(EquipSpecialAttribute.VESTIGE))
                return;

            final boolean safe = safeUpgrade ? equip.getChuc() >= 12 && equip.getChuc() <= 16 : safeUpgrade;

            final int reqLevel = template.getReqLevel();
            final boolean superior = template.isSuperiorEqp();
            final long moneyCost = HyperUpgradeConstants.getHyperUpgradeCost(reqLevel, equip.getChuc(), superior);
            final int successProp = HyperUpgradeConstants.getHyperSuccessProp(equip.getChuc(), superior);
            final int destroyProp = HyperUpgradeConstants.getHyperDestroyProp(equip.getChuc(), superior);

            HyperUpgradeInfo info = new HyperUpgradeInfo();

            info.setMoneyCost(moneyCost);
            info.setSuccessProp(successProp);
            info.setDestroyProp(destroyProp);
            info.setStats(HyperUpgradeConstants.getEnchantStats(equip));

            chr.write(new LP_EquipmentEnchantDisplay(new DisplayHyperUpgradeAction(info)));
        });
    }

    private void onUpgrade(final Character chr, int bagIndex) {

    }

}
