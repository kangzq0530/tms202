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

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public class WildHunterInfo {
    private int[] capturedMob = new int[5];
    private byte idx;
    private byte ridingType;

    public int[] getCapturedMob() {
        return Arrays.copyOfRange(capturedMob, 0, capturedMob.length);
    }

    public void setCapturedMob(int[] capturedMob) {
        this.capturedMob = Arrays.copyOfRange(capturedMob, 0, this.capturedMob.length);
    }

    public byte getIdx() {
        return idx;
    }

    public void setIdx(byte idx) {
        this.idx = idx;
    }

    public byte getRidingType() {
        return ridingType;
    }

    public void setRidingType(byte ridingType) {
        this.ridingType = ridingType;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        capturedMob[0] = 9304000;
        outPacket.encodeByte(10 * 1 + 1);
        for (int mob : getCapturedMob()) {
            outPacket.encodeInt(mob);
        }
    }

    public int getTemplateID() {
        int res = 0;
        if (getRidingType() > 0 && getRidingType() < 10) { // 1~9 has jaguars
            res = 9304000 + (getRidingType() - 1);
        }
        return res;
    }
}
