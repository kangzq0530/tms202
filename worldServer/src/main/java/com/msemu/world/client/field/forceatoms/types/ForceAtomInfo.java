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

package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.enums.FileTimeUnit;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public abstract class ForceAtomInfo {
    protected int count, inc, firstImpact, secondImpact, angle, startDelay,
            maxHitCount, effectIdx, arriveDir, arriveRange;
    protected Position start = new Position(0, 0);
    protected int num;
    protected FileTime createTime;

    public ForceAtomInfo(int inc, int firstImpact, int secondImpact, int angle, int startDelay) {
        this.inc = inc;
        this.firstImpact = firstImpact;
        this.secondImpact = secondImpact;
        this.angle = angle;
        this.startDelay = startDelay;
    }

    public boolean isExpired() {
        return getCreateTime().plus(5, FileTimeUnit.SEC).before(FileTime.now());
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(1);
        outPacket.encodeInt(this.getCount());
        outPacket.encodeInt(this.getInc());
        outPacket.encodeInt(this.getFirstImpact());
        outPacket.encodeInt(this.getSecondImpact());
        outPacket.encodeInt(this.getAngle());
        outPacket.encodeInt(this.getStartDelay());
        outPacket.encodeInt(this.getStart().getX());
        outPacket.encodeInt(this.getStart().getY());
        outPacket.encodeInt(this.getCreateTime().getLowDateTime());
        outPacket.encodeInt(this.getMaxHitCount());
        outPacket.encodeInt(this.getEffectIdx());
        outPacket.encodeInt(0);
    }
}
