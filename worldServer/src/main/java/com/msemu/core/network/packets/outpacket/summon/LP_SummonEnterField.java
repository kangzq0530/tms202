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

package com.msemu.core.network.packets.outpacket.summon;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.field.lifes.Summon;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_SummonEnterField extends OutPacket<GameClient> {

    public LP_SummonEnterField(Summon summon) {
        super(OutHeader.LP_SummonedEnterField);
        encodeInt(summon.getCharID());
        encodeInt(summon.getObjectId());
        encodeInt(summon.getSkillID());
        encodeByte(summon.getCharLevel());
        encodeByte(summon.getSlv());
        // CSummoned::Init
        encodePosition(summon.getPosition());
        encodeByte(summon.getAction());
        encodeShort(summon.getCurFoothold());
        encodeByte(summon.getMoveAbility());
        encodeByte(summon.getAssistType());
        encodeByte(summon.getEnterType());
        encodeInt(summon.getObjectId());
        encodeByte(summon.isFlyMob());
        encodeByte(summon.isBeforeFirstAttack());
        encodeInt(summon.getTemplateId());
        encodeInt(summon.getBulletID());
        AvatarLook avatarLook = summon.getAvatarLook();
        encodeByte(avatarLook != null);
        if (avatarLook != null) {
            avatarLook.encode(this);
        }
        if (summon.getSkillID() == 35111002) { // Tesla Coil
            encodeByte(summon.getTeslaCoilState());
            summon.getTeslaCoilPositions().forEach(this::encodePosition);
        }
        if (summon.getSkillID() == 42111003) { // Kishin Shoukan
            for (Position pos : summon.getKishinPositions()) {
                encodePosition(pos);
            }
        }
        encodeByte(summon.isJaguarActive());
        encodeInt(summon.getSummonTerm());
        encodeByte(summon.isAttackActive());
    }
}
