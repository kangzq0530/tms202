package com.msemu.world.client.character.skill;

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

    private void setLastUpdated(long l) {
        setLastUpdated(new FileTime(l));
    }

    public void setLastUpdated(FileTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
        outPacket.encodeInt(getExpireTerm());
        if (isDynamicTermSet()) {
            outPacket.encodeShort(getExpireTerm());
        }
    }
}