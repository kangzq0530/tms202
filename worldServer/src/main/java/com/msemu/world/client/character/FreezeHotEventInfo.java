package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/13.
 */

public class FreezeHotEventInfo {

    private int accountType;
    private int accountID;

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getAccountType());
        outPacket.encodeInt(getAccountID());
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}

