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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.InventoryManipulator;

/**
 * Created by Weber on 2018/4/28.
 */
public class CP_UserChangeSlotPositionRequest extends InPacket<GameClient> {

    private int tick = 0;
    private int invTypeVal = 0;
    private short srcSlot = -1;
    private short destSlot = -1;
    private short quantity = 0;

    public CP_UserChangeSlotPositionRequest(short opcode) {
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
            InventoryManipulator.move(chr, invType, srcSlot, destSlot);
        }
    }
}
