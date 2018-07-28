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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/13.
 */
public class CoupleRecord {
    private Map<Long, Integer> snToItemMap = new HashMap<>();
    private int pairCharacterId;
    private String pairCharacterName;
    private long sn;
    private long pairSn;
    private int itemID;

    public int getPairCharacterId() {
        return pairCharacterId;
    }

    public void setPairCharacterId(int pairCharacterId) {
        this.pairCharacterId = pairCharacterId;
    }

    public String getPairCharacterName() {
        return pairCharacterName;
    }

    public void setPairCharacterName(String pairCharacterName) {
        this.pairCharacterName = pairCharacterName;
    }

    public long getSn() {
        return sn;
    }

    public void setSn(long sn) {
        this.sn = sn;
    }

    public long getPairSn() {
        return pairSn;
    }

    public void setPairSn(long pairSn) {
        this.pairSn = pairSn;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getPairCharacterId());
        outPacket.encodeString(getPairCharacterName(), 15);
        outPacket.encodeLong(getSn());
        outPacket.encodeLong(getPairSn());
    }

    public void encodeForRemote(OutPacket outPacket) {
        outPacket.encodeInt(1); // there can be more of the following 3 things (combined)
        outPacket.encodeLong(getSn());
        outPacket.encodeLong(getPairSn());
        outPacket.encodeInt(getItemID());
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}

