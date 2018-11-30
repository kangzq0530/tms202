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

import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserSetActiveNickItem;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.ItemData;

/**
 * Created by Weber on 2018/5/23.
 */
public class CP_UserActivateNickItem extends InPacket<GameClient> {

    private int nickItemID;
    private int slotPos;

    public CP_UserActivateNickItem(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        nickItemID = decodeInt();
        slotPos = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();

        if (nickItemID == 0) {
            chr.enableActions();
            return;
        }
        final ItemTemplate itemInfo = ItemData.getInstance().getItemInfo(nickItemID);
        final Item item = chr.getInventoryByType(itemInfo.getInvType()).getItemBySlot(slotPos);

        if (item != null && item.getItemId() != itemInfo.getItemId()) {
            chr.enableActions();
            return;
        }
        final boolean hasItem = chr.getInventoryByType(itemInfo.getInvType())
                .containsItem(nickItemID);
        if (!hasItem)
            return;
        chr.setActiveNickItemID(nickItemID);
        field.broadcastPacket(new LP_UserSetActiveNickItem(chr, nickItemID, ""), chr);


    }
}
