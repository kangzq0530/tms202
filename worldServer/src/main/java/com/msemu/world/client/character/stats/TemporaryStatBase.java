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
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/11.
 */
public class TemporaryStatBase {
    protected int expireTerm;
    private Option option;
    private FileTime lastUpdated;
    private boolean dynamicTermSet;

    public TemporaryStatBase(boolean dynamicTermSet) {
        option = new Option();
        option.nOption = 0;
        option.rOption = 0;
        lastUpdated = new FileTime(System.currentTimeMillis());
        this.dynamicTermSet = dynamicTermSet;
    }

    public Option getOption() {
        return option;
    }

    public FileTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(FileTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private void setLastUpdated(long l) {
        setLastUpdated(new FileTime(l));
    }

    public int getExpireTerm() {
        if (isDynamicTermSet()) {
            return 1000 * expireTerm;
        }
        return Integer.MAX_VALUE;
    }

    public void setExpireTerm(int expireTerm) {
        this.expireTerm = expireTerm;
    }

    public boolean isDynamicTermSet() {
        return dynamicTermSet;
    }

    public void setDynamicTermSet(boolean dynamicTermSet) {
        this.dynamicTermSet = dynamicTermSet;
    }

    public int getMaxValue() {
        return 10000;
    }

    public boolean isActive() {
        return getOption().nOption >= 10000;
    }

    public boolean hasExpired(long time) {
        boolean result = false;
        if (isDynamicTermSet()) {
            result = getExpireTerm() > time - getLastUpdated().getLongValue();
        }
        return result;
    }

    public int getNOption() {
        return getOption().nOption;
    }

    public void setNOption(int i) {
        getOption().nOption = i;
    }

    public int getROption() {
        return getOption().rOption;
    }

    public void setROption(int reason) {
        getOption().rOption = reason;
    }

    public void reset() {
        getOption().nOption = 0;
        getOption().rOption = 0;
        setLastUpdated(System.currentTimeMillis());
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getOption().nOption);
        outPacket.encodeInt(getOption().rOption);
        outPacket.encodeByte(isDynamicTermSet());
        outPacket.encodeInt(getLastUpdated().getLowDateTime());
        if (isDynamicTermSet()) {
            outPacket.encodeShort(getExpireTerm());
        }
    }
}