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

package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.movement.IMovement;
import com.msemu.world.client.field.lifes.skills.MobSkillAttackInfo;

import java.util.List;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_MobMove extends OutPacket<GameClient> {

    public LP_MobMove(Mob mob, MobSkillAttackInfo msai, List<IMovement> movements) {
        super(OutHeader.LP_MobMove);
        encodeInt(mob.getObjectId());
        encodeByte(msai.getActionAndDirMask());
        encodeByte(msai.getActionAndDir());
        encodeInt(msai.getTargetInfo());
        encodeByte(msai.getMultiTargetForBalls().size());
        msai.getMultiTargetForBalls().forEach(this::encodePosition);
        encodeByte(msai.getRandTimeForAreaAttacks().size());
        msai.getRandTimeForAreaAttacks().forEach(this::encodeShort);
        encodeInt(msai.getEncodedGatherDuration());
        encodePosition(msai.getOldPos());
        encodePosition(msai.getOldVPos());
        encodeByte(movements.size());
        movements.stream().forEach(m -> m.encode(this));
        encodeByte(0);
        encodeByte(0);
    }
}
