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
import com.msemu.core.network.packets.outpacket.user.common.LP_ShowItemReleaseEffect;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.InventoryManipulator;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;

import static com.msemu.world.enums.ChatMsgType.MOB;

public class CP_UserItemReleaseRequest extends InPacket<GameClient> {

    private int updateTick;
    private short uPos;
    private short ePos;

    public CP_UserItemReleaseRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        uPos = decodeShort();
        ePos = decodeShort();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Item magnify = chr.getConsumeInventory().getItemBySlot(uPos);
        final Equip equip = (Equip) (ePos < 0 ? chr.getEquippedInventory().getItemBySlot(-ePos)
                : chr.getEquipInventory().getItemBySlot(ePos));
        if (equip == null) {
            // messag
            return;
        }
        final boolean base = equip.getOptionBase(0) < 0;
        final boolean bonus = equip.getOptionBonus(0) < 0;
        if (base && bonus) {
            equip.releaseOptions(true);
            equip.releaseOptions(false);
        } else {
            equip.releaseOptions(bonus);
        }
        chr.chatMessage(MOB, "Grade = " + equip.getGrade());
        for (int i = 0; i < 6; i++) {
            chr.chatMessage(MOB, "Opt " + i + " = " + equip.getOptions().get(i));
        }
        chr.write(new LP_ShowItemReleaseEffect(chr, ePos, bonus));
        InventoryManipulator.update(chr, equip.getInvType(), equip.getBagIndex());
    }
}
