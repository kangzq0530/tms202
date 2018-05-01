package com.msemu.core.network.packets.in;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.InventoryManipulator;

/**
 * Created by Weber on 2018/4/28.
 */
public class UserChangeSlotPositionRequest extends InPacket<GameClient> {

    private int tick = 0;
    private int invTypeVal = 0;
    private short srcSlot = -1;
    private short destSlot = -1;
    private short quantity = 0;

    public UserChangeSlotPositionRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        tick = decodeInt();
        invTypeVal = decodeByte();
        srcSlot = decodeShort();
        destSlot = decodeShort();
        quantity = decodeShort();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        InvType invType = InvType.getInvTypeByValue(invTypeVal);
        if (srcSlot < 0 && destSlot > 0) {
            InventoryManipulator.unequip(chr, srcSlot, destSlot);
        } else if (destSlot < 0) {
            InventoryManipulator.equip(chr, srcSlot, destSlot);
        } else if (destSlot == 0) {
            InventoryManipulator.drop(chr, invType, srcSlot, destSlot, quantity);
        } else {
            InventoryManipulator.move(chr, invType, srcSlot, destSlot, quantity);
        }
    }
}
