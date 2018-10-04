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
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.miniroom.MiniRoom;
import com.msemu.world.client.character.miniroom.MiniRoomFactory;
import com.msemu.world.client.character.miniroom.TradeRoom;
import com.msemu.world.enums.MiniRoomOperation;
import com.msemu.world.enums.MiniRoomType;

public class CP_MiniRoom extends InPacket<GameClient> {

    private int opcodeValue, miniRoomTypeValue, invTypeValue, srcSlot, quantity, targetSlot;

    public CP_MiniRoom(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        opcodeValue = decodeByte();

        final MiniRoomOperation opcode = MiniRoomOperation.getByValue(opcodeValue);
        switch (opcode) {
            case TRP_PutItem1:
            case TRP_PutItem2:
            case TRP_PutItem3:
            case TRP_PutItem4:
                invTypeValue = decodeByte();
                srcSlot = decodeShort();
                quantity = decodeShort();
                targetSlot = decodeByte();
                break;
            case MRP_Create:
                miniRoomTypeValue = decodeByte();
                break;
        }
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final MiniRoomOperation opcode = MiniRoomOperation.getByValue(opcodeValue);
        final MiniRoom miniRoom;

        switch (opcode) {
            case TRP_PutItem1:
            case TRP_PutItem2:
            case TRP_PutItem3:
            case TRP_PutItem4:
                miniRoom = chr.getMiniRoom();
                if (miniRoom instanceof TradeRoom) {
                    final InvType invType = InvType.getInvTypeByValue(invTypeValue);
                    final Inventory inventory = chr.getInventoryByType(invType);
                    final Item item = inventory.getItemBySlot(srcSlot);
                    if (item == null || item.getQuantity() < quantity) {
                        return;
                    }
//
                }
                break;
            case TRP_PutMoney1:
            case TRP_PutMoney2:
            case TRP_PutMoney3:
            case TRP_PutMoney4:
                break;
            case TRP_Trade1: // 確認交易
            case TRP_Trade2:
            case TRP_Trade3:
            case TRP_Trade4:
                miniRoom = chr.getMiniRoom();
                if (miniRoom instanceof TradeRoom) {
                    ((TradeRoom) miniRoom).completeTrade(chr);
                }
                break;
            case MRP_Enter:
                break;
            case MRP_Create:
                final MiniRoomType miniRoomType = MiniRoomType.getByValue(miniRoomTypeValue);
                miniRoom = MiniRoomFactory.getMiniRoom(miniRoomType);
                if (miniRoom != null) {
                    chr.setMiniRoom(miniRoom);
                    miniRoom.create(chr);
                }
                break;
        }

    }
}
