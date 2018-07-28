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
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.enums.AvatarModifiedMask;

/**
 * Created by Weber on 2018/4/28.
 */
public class LP_UserAvatarModified extends OutPacket<GameClient> {

    public LP_UserAvatarModified(Character chr, byte mask, boolean carryItemEffect) {
        super(OutHeader.LP_UserAvatarModified);
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        encodeInt(chr.getId());
        encodeByte(mask);

        if ((mask & AvatarModifiedMask.AvatarLook.getVal()) != 0) {
            al.encode(this);
        }
        if ((mask & AvatarModifiedMask.SubAvatarLook.getVal()) != 0) {
            al.encode(this);
        }
        if ((mask & AvatarModifiedMask.Speed.getVal()) != 0) {
            encodeByte(tsm.getOption(CharacterTemporaryStat.Speed).nOption);
        }
        if ((mask & AvatarModifiedMask.CarryItemEffect.getVal()) != 0) {
            encodeByte(carryItemEffect);
        }
        boolean hasCouple = chr.getCouple() != null;
        encodeByte(hasCouple);
        if (hasCouple) {
            chr.getCouple().encodeForRemote(this);
        }
        encodeByte(chr.getFriendshipRingRecord().size());
        chr.getFriendshipRingRecord().forEach(ring -> ring.encode(this));
        boolean hasWedding = chr.getMarriageRecord() != null;
        encodeByte(hasWedding);
        if (hasWedding) {
            chr.getMarriageRecord().encodeForRemote(this);
        }
        encodeInt(0);
        encodeInt(chr.getCompletedSetItemID());
        encodeInt(chr.getTotalChuc());
    }

}
