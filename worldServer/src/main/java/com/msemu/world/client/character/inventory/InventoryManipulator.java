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

package com.msemu.world.client.character.inventory;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_InventoryOperation;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.data.ItemData;
import com.msemu.world.enums.InventoryOperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/28.
 */
public class InventoryManipulator {
    public static void unequip(Character chr, short srcSlot, short destSlot) {
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        List<InventoryOperationInfo> operates = new ArrayList<>();
        Equip srcEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(-srcSlot);
        Equip destEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(destSlot);
        if (destSlot <= 0 || srcEquip == null || srcSlot == -55
                || (destEquip != null && srcSlot <= 0))
            return;
        chr.chatMessage(String.format("[卸下裝備] 來源: %d 道具: %s(%d)",
                srcSlot,
                srcEquip.getTemplate().getName(),
                srcEquip.getTemplate().getItemId()
                )
        );
        chr.getEquippedInventory().removeItem(srcEquip);
        chr.getEquipInventory().addItem(srcEquip);
        srcEquip.setBagIndex(destSlot);
        List<Integer> hairEquips = al.getHairEquips();
        if (ItemConstants.類型.武器(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setWeaponId(0);
        } else if (ItemConstants.類型.副手(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setSubWeaponId(0);
        }
        if (hairEquips.contains(srcEquip.getItemId())) {
            hairEquips.remove(new Integer(srcEquip.getItemId()));
        }
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcEquip, srcSlot));
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewAvatarLook();
        chr.renewCharacterStats();
    }

    public static void equip(Character chr, short srcSlot, short destSlot) {
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        List<InventoryOperationInfo> operates = new ArrayList<>();
        Equip srcEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(srcSlot);
        Equip destEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(-destSlot);
        List<Integer> hairEquips = al.getHairEquips();
        chr.chatMessage(String.format("[穿上裝備] 來源: %d 道具: %s(%d) 目標: %d 道具: %s(%d)",
                srcSlot,
                srcEquip != null ? srcEquip.getTemplate().getName() : "無",
                srcEquip != null ? srcEquip.getTemplate().getItemId() : 0,
                destSlot,
                destEquip != null ? destEquip.getTemplate().getName() : "無",
                destEquip != null ? destEquip.getTemplate().getItemId() : 0
        ));
        if (srcEquip == null || ItemConstants.類型.isHarvesting(srcEquip.getItemId())) {
            chr.enableActions();
            return;
        }
        if (destEquip != null) {
            short nextSlot = (short) chr.getEquipInventory().getFirstOpenSlot();
            if (nextSlot > 0)
                unequip(chr, destSlot, nextSlot);
            else {
                chr.enableActions();
                return;
            }
        }
        chr.getEquipInventory().removeItem(srcEquip);
        chr.getEquippedInventory().addItem(srcEquip);
        srcEquip.setBagIndex(destSlot);
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcEquip, srcSlot));
        if (!hairEquips.contains(srcEquip.getItemId())) {
            hairEquips.add(srcEquip.getItemId());
        }
        if (ItemConstants.類型.武器(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setWeaponId(srcEquip.getItemId());
        } else if (ItemConstants.類型.副手(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setSubWeaponId(srcEquip.getItemId());
        }
        chr.write(new LP_InventoryOperation(true, false, operates));
        chr.renewAvatarLook();
        chr.renewCharacterStats();
    }

    public static void drop(Character chr, InvType invType, short srcSlot, short destSlot, short quantity) {
        Item srcItem = chr.getInventoryByType(invType).getItemBySlot(srcSlot);
        Drop drop;

        List<InventoryOperationInfo> operates = new ArrayList<>();
        if (!srcItem.getInvType().isStackable() || quantity >= srcItem.getQuantity()) {
            chr.getInventoryByType(invType).removeItem(srcItem);
            srcItem.drop();
            drop = new Drop(-1, srcItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.REMOVE,
                    srcItem, srcSlot));
        } else {
            Item dropItem = ItemData.getInstance().createItem(srcItem.getItemId());
            dropItem.setQuantity(quantity);
            srcItem.removeQuantity(quantity);
            srcItem.setInventoryId(0);
            drop = new Drop(-1, dropItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.UPDATE,
                    srcItem, srcSlot));
        }
        chr.chatMessage(String.format("[丟棄裝備] 來源: %d 道具: %s(%d)",
                srcSlot,
                drop.getItem().getTemplate().getName(),
                drop.getItem().getTemplate().getItemId()));

        ItemTemplate t = srcItem.getTemplate();
        if (t.isTradeBlock() || t.isQuest()) {
            chr.getField().dropFadeOut(drop, chr.getPosition());
        } else {
            chr.getField().drop(drop, chr.getPosition());
        }
        chr.write(new LP_InventoryOperation(true, false, operates));
    }

    public static void move(Character chr, InvType invType, short srcSlot, short destSlot) {
        if (srcSlot < 0 || destSlot < 0 || srcSlot == destSlot || invType == InvType.EQUIPPED)
            return;
        Item srcItem = chr.getInventoryByType(invType).getItemBySlot(srcSlot);
        Item destItem = chr.getInventoryByType(invType).getItemBySlot(destSlot);
        boolean bag = false, switchSrcDst = false, bothBag = false;

        // TODO 放入/取出擴充背包( 椅子背包、卷軸背包 ... etc 位置貌似在 100 up
        if (destSlot > chr.getInventoryByType(invType).getSlots()) {
            chr.enableActions();
            return;
        }
        if (srcSlot > chr.getInventoryByType(invType).getSlots()) {
            chr.enableActions();
            return;
        }
        chr.chatMessage(String.format("[移動裝備] 來源: %d 道具: %s(%d) 目標: %d 道具:  %s(%d)",
                srcSlot,
                srcItem != null ? srcItem.getTemplate().getName() : "無",
                srcItem != null ? srcItem.getTemplate().getItemId() : 0,
                destSlot,
                destItem != null ? destItem.getTemplate().getName() : "無",
                destItem != null ? destItem.getTemplate().getItemId() : 0
        ));

        List<InventoryOperationInfo> operates = new ArrayList<>();

        if (destItem != null) {
            destItem.setBagIndex(srcSlot);
        }


        srcItem.setBagIndex(destSlot);

        chr.getInventoryByType(invType).sortItemsByIndex();
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcItem, srcSlot));

        chr.write(new LP_InventoryOperation(true, false, operates));
    }

    public static void update(final Character chr, final InvType invType, final int pos) {
        final Item item = chr.getInventoryByType(invType).getItemBySlot(pos);
        if (item == null)
            return;
        List<InventoryOperationInfo> operates = new ArrayList<>();
        operates.add(new InventoryOperationInfo(InventoryOperationType.ADD, item, item.getBagIndex()));
        chr.write(new LP_InventoryOperation(true, false, operates));
    }
}
