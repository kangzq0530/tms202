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
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class ItemPot {

    private int lifeID;

    private byte level;

    private byte lastState;

    private int satiety;

    private int friendly;

    private int remainAbleFriendly;

    private int remainFriendlyTime;

    private byte maximumIncLevel;

    private int maximumIncSatiety;

    private int lastEatTime;

    private FileTime lastSleepStartTime;

    private FileTime lastDecSatietyTime;

    private FileTime consumedLastTime;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getLifeID());
        outPacket.encodeByte(getLevel());
        outPacket.encodeByte(getLastState());
        outPacket.encodeInt(getSatiety());
        outPacket.encodeInt(getFriendly());
        outPacket.encodeInt(getRemainAbleFriendly());
        outPacket.encodeInt(getRemainFriendlyTime());
        outPacket.encodeByte(getMaximumIncLevel());
        outPacket.encodeInt(getMaximumIncSatiety());
        outPacket.encodeFT(getLastEatTime());
        outPacket.encodeFT(getLastSleepStartTime());
        outPacket.encodeFT(getLastDecSatietyTime());
        outPacket.encodeFT(getConsumedLastTime());


    }
}

