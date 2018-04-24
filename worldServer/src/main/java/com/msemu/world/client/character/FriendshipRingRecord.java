package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/13.
 */
public class FriendshipRingRecord {
    private long friendshipItemSN;
    private long friendshipPairItemSN;
    private int itemID;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(1); // can be more of the following 3 properties
        outPacket.encodeLong(getFriendshipItemSN());
        outPacket.encodeLong(getFriendshipPairItemSN());
        outPacket.encodeInt(getItemID());
    }

    public long getFriendshipItemSN() {
        return friendshipItemSN;
    }

    public void setFriendshipItemSN(long friendshipItemSN) {
        this.friendshipItemSN = friendshipItemSN;
    }

    public long getFriendshipPairItemSN() {
        return friendshipPairItemSN;
    }

    public void setFriendshipPairItemSN(long friendshipPairItemSN) {
        this.friendshipPairItemSN = friendshipPairItemSN;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}

