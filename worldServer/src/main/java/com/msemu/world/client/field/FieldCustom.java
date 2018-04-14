package com.msemu.world.client.field;

import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/14.
 */
public class FieldCustom {
    private int partyBonusExpRate;
    private String bgm;
    private int bgFieldID;

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getPartyBonusExpRate());
        outPacket.encodeString(getBgm());
        outPacket.encodeInt(getBgFieldID());
    }

    public int getPartyBonusExpRate() {
        return partyBonusExpRate;
    }

    public void setPartyBonusExpRate(int partyBonusExpRate) {
        this.partyBonusExpRate = partyBonusExpRate;
    }

    public String getBgm() {
        return bgm;
    }

    public void setBgm(String bgm) {
        this.bgm = bgm;
    }

    public int getBgFieldID() {
        return bgFieldID;
    }

    public void setBgFieldID(int bgFieldID) {
        this.bgFieldID = bgFieldID;
    }
}
