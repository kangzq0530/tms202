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

package com.msemu.world.client.character.stats;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/11.
 */
public class PartyBooster extends TwoStateTemporaryStat {

    private int currentTime;

    public PartyBooster() {
        super(false);
        currentTime = 0;
        expireTerm = 0;
    }

    @Override
    public int getExpireTerm() {
        return 1000 * expireTerm;
    }

    @Override
    public boolean hasExpired(long tCur) {
        return getExpireTerm() < tCur - getCurrentTime();
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void reset() {
        super.reset();
        setCurrentTime(0);
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
        outPacket.encodeByte(getNOption() == 0 ? 0 : 1);
        outPacket.encodeInt(getCurrentTime());
        outPacket.encodeShort(getExpireTerm());
    }
}