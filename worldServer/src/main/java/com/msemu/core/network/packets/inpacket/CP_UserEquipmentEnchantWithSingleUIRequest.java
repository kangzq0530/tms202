package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.enhant.EquipmentEnchantScroll;
import com.msemu.world.enums.EquipmentEnchantType;

public class CP_UserEquipmentEnchantWithSingleUIRequest extends InPacket<GameClient> {


    private int type;

    private int itemSlot;

    public CP_UserEquipmentEnchantWithSingleUIRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        type = decodeByte();

        final EquipmentEnchantType enchantType = EquipmentEnchantType.getByValue(type);

        switch (enchantType) {
            case SCROLL_REQUEST_DISPLAY:
                itemSlot = decodeInt();
                break;
        }
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final EquipmentEnchantType enchantType = EquipmentEnchantType.getByValue(type);

        switch (enchantType) {


            case SCROLL_REQUEST_DISPLAY:
                Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(itemSlot);
                if( equip == null )
                    return;
                EquipmentEnchantScroll.onRequestDisplay(chr, equip);
                break;
        }
    }
}
