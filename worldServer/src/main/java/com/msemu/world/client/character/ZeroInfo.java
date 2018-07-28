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
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */

@Getter
@Setter
public class ZeroInfo {
    private boolean isZeroBetaState;
    private int subHP;
    private int subMHP;
    private int subMP;
    private int subMMP;
    private int subSkin;
    private int subHair;
    private int subFace;
    private int dbCharZeroLinkCashPart;
    private int mixBaseHairColor;
    private int mixAddHairColor;
    private int mixHairBaseProb;

    public ZeroInfo deepCopy() {
        ZeroInfo zi = new ZeroInfo();
        zi.setZeroBetaState(isZeroBetaState());
        zi.setSubHP(getSubHP());
        zi.setSubMHP(getSubMHP());
        zi.setSubMP(getSubMP());
        zi.setSubMMP(getSubMMP());
        zi.setSubSkin(getSubSkin());
        zi.setSubHair(getSubHair());
        zi.setSubFace(getSubFace());
        zi.setDbCharZeroLinkCashPart(getDbCharZeroLinkCashPart());
        zi.setMixBaseHairColor(getMixBaseHairColor());
        zi.setMixAddHairColor(getMixAddHairColor());
        zi.setMixHairBaseProb(getMixHairBaseProb());
        return zi;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        short mask = 0x1 | 0x2 | 0x4 | 0x40 | 0x80;
        outPacket.encodeShort(mask);
        if ((mask & 0x1) != 0) {
            outPacket.encodeByte(true);
        }
        if ((mask & 0x2) != 0) {
            outPacket.encodeInt(getSubHP() / 2);
        }
        if ((mask & 0x4) != 0) {
            outPacket.encodeInt(getSubMP() / 3);
        }
        if ((mask & 0x8) != 0) {
            outPacket.encodeByte(getSubSkin());
        }
        if ((mask & 0x10) != 0) {
            outPacket.encodeInt(getSubHair());
        }
        if ((mask & 0x20) != 0) {
            outPacket.encodeInt(getSubFace());
        }
        if ((mask & 0x40) != 0) {
            outPacket.encodeInt(getSubMHP());
        }
        if ((mask & 0x80) != 0) {
            outPacket.encodeInt(getSubMMP());
        }
        if ((mask & 0x100) != 0) {
            outPacket.encodeInt(getDbCharZeroLinkCashPart());
        }
        if ((mask & 0x200) != 0) {
            outPacket.encodeInt(getMixBaseHairColor());
            outPacket.encodeInt(getMixAddHairColor());
            outPacket.encodeInt(getMixHairBaseProb());
        }
    }
}
