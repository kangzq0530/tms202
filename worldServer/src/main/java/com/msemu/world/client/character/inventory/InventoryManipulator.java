package com.msemu.world.client.character.inventory;

import com.msemu.commons.data.enums.InvType;
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
        chr.chatMessage(String.format("[穿上裝備] 來源: %d 目標: %d", srcSlot, destSlot));
        List<InventoryOperationInfo> operates = new ArrayList<>();
        Equip srcEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(srcSlot);
        Equip destEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(-destSlot);
        List<Integer> hairEquips = al.getHairEquips();
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
        chr.chatMessage(String.format("[丟棄裝備] 來源: %d 目標: %d", srcSlot, destSlot));
        List<InventoryOperationInfo> operates = new ArrayList<>();
        if (!srcItem.getInvType().isStackable() || quantity >= srcItem.getQuantity()) {
            chr.getInventoryByType(invType).removeItem(srcItem);
            srcItem.drop();
            drop = new Drop(-1, srcItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.REMOVE,
                    srcItem, srcSlot));
        } else {
            Item dropItem = ItemData.getInstance().getItemFromTemplate(srcItem.getItemId());
            dropItem.setQuantity(quantity);
            srcItem.removeQuantity(quantity);
            drop = new Drop(-1, dropItem);
            operates.add(new InventoryOperationInfo(InventoryOperationType.UPDATE,
                    srcItem, srcSlot));
        }
        chr.getField().drop(drop, chr.getPosition());
        chr.write(new LP_InventoryOperation(true, false, operates));
    }

    public static void move(Character chr, InvType invType, short srcSlot, short destSlot) {
        if (srcSlot < 0 || destSlot < 0 || srcSlot == destSlot || invType == InvType.EQUIPPED)
            return;
        chr.chatMessage(String.format("[移動裝備] 來源: %d 目標: %d", srcSlot, destSlot));
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
        List<InventoryOperationInfo> operates = new ArrayList<>();

        if (destItem != null) {
            destItem.setBagIndex(srcSlot);
            operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                    destItem, destSlot));
        }
        srcItem.setBagIndex(destSlot);

        chr.getInventoryByType(invType).sortItemsByIndex();
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcItem, srcSlot));

        chr.write(new LP_InventoryOperation(true, false, operates));
    }
}
