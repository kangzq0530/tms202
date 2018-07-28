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
                if (equip == null)
                    return;
                EquipmentEnchantScroll.onRequestDisplay(chr, equip);
                break;
        }
    }
}
