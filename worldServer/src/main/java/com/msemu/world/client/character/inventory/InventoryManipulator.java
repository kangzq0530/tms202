package com.msemu.world.client.character.inventory;

import com.msemu.commons.data.enums.InvType;
import com.msemu.core.network.packets.out.wvscontext.InventoryOperation;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.constants.ItemConstants;
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
                .getItemBySlot(srcSlot);
        Equip destEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(destSlot);
        if(destSlot < 0 || srcEquip == null || srcSlot == -55
                || (destEquip != null && srcSlot <= 0))
            return;

        chr.getEquippedInventory().removeItem(srcEquip);
        chr.getEquipInventory().addItem(srcEquip);
        srcEquip.setBagIndex(srcSlot);
        List<Integer> hairEquips = al.getHairEquips();
        if(ItemConstants.類型.武器(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setWeaponId(0);
        } else if(ItemConstants.類型.副手(srcEquip.getItemId())) {
            chr.getAvatarData().getAvatarLook().setSubWeaponId(0);
        }
        if(hairEquips.contains(srcEquip.getItemId())) {
            hairEquips.remove(srcEquip.getItemId());
        }
        operates.add(new InventoryOperationInfo(InventoryOperationType.MOVE,
                srcEquip, srcSlot));
        chr.write(new InventoryOperation(true, false, operates));
        chr.renewAvatarLook();
        chr.renewCharacterStats();
        chr.enableActions();
        if(destEquip != null)
            equip(chr, destSlot, srcSlot);
    }

    public static void equip(Character chr, short srcSlot, short destSlot) {
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        List<InventoryOperationInfo> operates = new ArrayList<>();
        Equip srcEquip = (Equip) chr.getEquipInventory()
                .getItemBySlot(srcSlot);
        Equip destEquip = (Equip) chr.getEquippedInventory()
                .getItemBySlot(destSlot);

        if(srcEquip == null || ItemConstants.類型.isHarvesting(srcEquip.getItemId())) {
            chr.enableActions();
            return;
        }



    }

    public static void drop(Character chr, InvType invType, short srcSlot, short destSlot, short quantity) {
        
    }

    public static void move(Character chr, InvType invType, short srcSlot, short destSlot, short quantity) {
    }
}
