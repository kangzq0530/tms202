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

package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;

/**
 * Created by Weber on 2018/5/15.
 */
public class LP_UserSetActivePortableChair extends OutPacket<GameClient> {

    public LP_UserSetActivePortableChair(Character chr, Item chairItem, String chairString) {
        super(OutHeader.LP_UserSetActivePortableChair);
        encodeInt(chr.getId());
        encodeInt(chairItem.getItemId());
        boolean btextChir = chairString == null || chairString.isEmpty();
        encodeByte(btextChir);
        if (btextChir) {
            encodeString(chairString);
        }
        int towerChairSize = 0;
        for (int i = 0; i < towerChairSize; i++) {
            encodeInt(0);
        }
        boolean unk1 = false;
        encodeByte(unk1);
        if (unk1) {
            encodeInt(0);
            encodeInt(0);
        }
        int unk2 = 0;
        encodeInt(unk2);
        encodeByte(false);
        encodeInt(0);
        encodeInt(0);
    }
}
