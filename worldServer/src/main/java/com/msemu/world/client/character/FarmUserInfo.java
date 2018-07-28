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

/**
 * Created by Weber on 2018/4/13.
 */
public class FarmUserInfo {
    private String farmName = "Kappa";
    private int farmPoint;
    private int farmLevel;
    private int farmExp;
    private int decoPoint;
    private int farmCash;
    private byte farmGender;
    private int farmTheme;
    private int slotExtend;
    private int lockerSlotCount;

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public int getFarmPoint() {
        return farmPoint;
    }

    public void setFarmPoint(int farmPoint) {
        this.farmPoint = farmPoint;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getDecoPoint() {
        return decoPoint;
    }

    public void setDecoPoint(int decoPoint) {
        this.decoPoint = decoPoint;
    }

    public int getFarmCash() {
        return farmCash;
    }

    public void setFarmCash(int farmCash) {
        this.farmCash = farmCash;
    }

    public byte getFarmGender() {
        return farmGender;
    }

    public void setFarmGender(byte farmGender) {
        this.farmGender = farmGender;
    }

    public int getFarmTheme() {
        return farmTheme;
    }

    public void setFarmTheme(int farmTheme) {
        this.farmTheme = farmTheme;
    }

    public int getSlotExtend() {
        return slotExtend;
    }

    public void setSlotExtend(int slotExtend) {
        this.slotExtend = slotExtend;
    }

    public int getLockerSlotCount() {
        return lockerSlotCount;
    }

    public void setLockerSlotCount(int lockerSlotCount) {
        this.lockerSlotCount = lockerSlotCount;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(getFarmName());
        outPacket.encodeInt(getFarmPoint());
        outPacket.encodeInt(getFarmLevel());
        outPacket.encodeInt(getFarmExp());
        outPacket.encodeInt(getDecoPoint());
        outPacket.encodeInt(getFarmCash());
        outPacket.encodeByte(getFarmGender());
        outPacket.encodeInt(getFarmTheme());
        outPacket.encodeInt(getSlotExtend());
        outPacket.encodeInt(getLockerSlotCount());
    }
}