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

package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.AffectedArea;
import com.msemu.world.constants.SkillConstants;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_AffectedAreaCreated extends OutPacket<GameClient> {

    public LP_AffectedAreaCreated(AffectedArea aa) {
        super(OutHeader.LP_AffectedAreaCreated);

        encodeInt(aa.getObjectId());
        encodeByte(aa.getMobOrigin());
        encodeInt(aa.getCharID());
        encodeInt(aa.getSkillID());
        encodeByte(aa.getSlv());
        encodeShort(aa.getDelay());
        encodeRect(aa.getRect());
        encodeInt(aa.getElemAttr());
        encodeInt(aa.getElemAttr()); // ?
        encodePosition(aa.getPosition());
        encodeInt(aa.getForce());
        encodeInt(aa.getOption());
        encodeByte(aa.getOption() != 0);
        encodeInt(aa.getIdk()); // ?
        if (SkillConstants.isFlipAffectAreaSkill(aa.getSkillID())) {
            encodeByte(aa.isFlip());
        }
        encodeInt(0); // ?

    }
}
