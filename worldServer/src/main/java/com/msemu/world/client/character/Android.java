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

package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.android.LP_AndroidEnterField;
import com.msemu.core.network.packets.outpacket.user.android.LP_AndroidLeaveField;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.lifes.AbstractAnimatedFieldLife;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Weber on 2018/5/22.
 */
public class Android extends AbstractAnimatedFieldLife {

    @Getter
    private Character owner;

    @Getter
    private int type;

    @Getter
    private int itemId;

    @Getter
    private AndroidInfo androidInfo = new AndroidInfo();

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.ANDROID;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_AndroidEnterField(this));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_AndroidLeaveField(this));
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getOwner().getId());
        outPacket.encodeByte(getType());
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getAction());
        outPacket.encodeShort(getFh());
        getAndroidInfo().encode(outPacket);
        List<Item> equippedItems = getOwner().getEquippedInventory().getItems();
        equippedItems.sort(Comparator.comparingInt(Item::getBagIndex));
        equippedItems.forEach(item -> {
            Equip equip = (Equip) item;
            if (item.getItemId() >= 1200 && item.getId() < 1208) {
                outPacket.encodeInt(equip.getItemId());
                outPacket.encodeInt(equip.getOptions().get(6));
            }
        });
    }
}
