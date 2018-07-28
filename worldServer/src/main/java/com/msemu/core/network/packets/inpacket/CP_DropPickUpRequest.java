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
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.effect.LP_UserEffectLocal;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_Message;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.effect.PickUpItemUserEffect;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.messages.DropPickUpAddIenventoryItem;
import com.msemu.world.client.character.messages.DropPickUpDuplicateItemSN;
import com.msemu.world.client.character.messages.DropPickUpItemFullFail;
import com.msemu.world.client.character.messages.DropPickUpOwnerShipFail;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.ItemConstants;

/**
 * Created by Weber on 2018/5/16.
 */
public class CP_DropPickUpRequest extends InPacket<GameClient> {

    private byte fieldKey;
    private int updateTick;
    private Position position;
    private int objectID;
    private int itemCrc;
    private boolean forgeWindow;
    private int forgeWindowHWND;
    private int gameWindowHWND;

    public CP_DropPickUpRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        fieldKey = decodeByte();
        updateTick = decodeInt();
        position = decodePosition();
        objectID = decodeInt();
        itemCrc = decodeInt();
        forgeWindow = decodeByte() > 0;
        forgeWindowHWND = decodeInt();
        gameWindowHWND = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();

        Drop drop = field.getDropByObjectId(objectID);

        drop.getLock().lock();
        try {
            if (drop.isPicked()) {
                chr.write(new LP_Message(new DropPickUpDuplicateItemSN()));
            } else if (drop.getOwnerID() > 0 && drop.getOwnerID() != chr.getId()) {
                chr.write(new LP_Message(new DropPickUpOwnerShipFail()));
                chr.enableActions();
            } else {
                if (drop.isMoney()) {
                    boolean canHoldMoney = chr.getMoney() + drop.getMoney() > GameConstants.MAX_MONEY;
                    if (!canHoldMoney) {
                        chr.write(new LP_Message(new DropPickUpItemFullFail()));
                        return;
                    } else {
                        chr.addMoney(drop.getMoney(), false);
                    }
                } else {
                    Item item = drop.getItem();
                    if (!chr.canHold(item.getItemId(), item.getQuantity())) {
                        chr.write(new LP_Message(new DropPickUpItemFullFail()));
                        return;
                    } else {
                        if (ItemConstants.isEquip(item.getItemId())) {
                            Equip equip = (Equip) item;
                            // 有潛能的道具，顯示特效
                            if (equip.getBonusGrade() > 0 || equip.getBaseGrade() > 0) {
                                chr.write(new LP_UserEffectLocal(new PickUpItemUserEffect(item)));
                            }
                        }
                        chr.write(new LP_Message(new DropPickUpAddIenventoryItem(item)));
                        chr.addItemToInventory(item);
                    }
                }
                drop.setPicked(true);
                drop.setPickupId(chr.getId());
                field.removeDropFromUser(drop);
            }
        } finally {
            drop.getLock().unlock();
        }
    }
}
