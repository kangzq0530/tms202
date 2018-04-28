package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/13.
 */
public class FriendRecord {
    private int pairCharacterId;
    private String pairCharacterName;
    private long sn;
    private long pairSn;
    private int friendItemId;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getPairCharacterId());
        outPacket.encodeString(getPairCharacterName(), 15);
        outPacket.encodeLong(getSn());
        outPacket.encodeLong(getPairSn());
        outPacket.encodeInt(getFriendItemId());
    }

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

    public int getFriendItemId() {
        return friendItemId;
    }

    public void setFriendItemId(int friendItemId) {
        this.friendItemId = friendItemId;
    }
}
