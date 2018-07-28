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

package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.constants.ItemConstants;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/5/23.
 */
public class LP_CharacterInfo extends OutPacket<GameClient> {

    public LP_CharacterInfo(Character chr, boolean self) {
        super(OutHeader.LP_CharacterInfo);
        final CharacterStat cs = chr.getAvatarData().getCharacterStat();
        encodeInt(chr.getId());
        encodeByte(0);
        encodeByte(chr.getLevel());
        encodeShort(chr.getJob());
        encodeShort(chr.getSubJob());
        encodeByte(cs.getPvpGrade());
        encodeInt(cs.getPop());
        boolean hasMarriage = chr.getMarriageRecord() != null;
        encodeByte(hasMarriage);
        if (hasMarriage) {
            chr.getMarriageRecord().encode(this);
        }
        //TODO 專業技能
        encodeByte(0); // short for loop
        boolean hasGuild = chr.getGuild() != null;
        if (hasGuild) {
            encodeString(chr.getGuild().getName());
            encodeString(""); // TODO allaince Name
        } else {
            encodeString("");
            encodeString("");
        }
        encodeByte(self ? -1 : 0);
        encodeByte(0);
        encodeString("");
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);

        encodeByte(0); // pet

        encodeByte(0); // wish list - int for loop

        Item medal = chr.getEquippedInventory().getItemBySlot(46);
        encodeInt(medal != null ? medal.getItemId() : 0);
        encodeShort(0); // medal Quests

        encodeByte(0); // damageSkin


        encodeZeroBytes(6 * 4); // traits level

        List<Item> chairs = chr.getInstallInventory().getItems()
                .stream()
                .filter(item -> ItemConstants.類型.椅子(item.getItemId()))
                .collect(Collectors.toList());

        encodeInt(chairs.size());
        chairs.forEach(item -> encodeInt(item.getItemId()));

        List<Item> medals = chr.getEquipInventory().getItems()
                .stream()
                .filter(item -> item.getItemId() >= 1142000 && item.getItemId() < 1152000)
                .collect(Collectors.toList());

        encodeInt(medals.size());
        medals.forEach(item -> encodeInt(item.getItemId()));

        //tODOQQ

    }
}
