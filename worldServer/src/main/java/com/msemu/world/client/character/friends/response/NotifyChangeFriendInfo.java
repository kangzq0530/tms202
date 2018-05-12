package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.friends.FriendEntry;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class NotifyChangeFriendInfo implements IFriendResponse {

    private FriendEntry entry;

    public NotifyChangeFriendInfo(FriendEntry entry) {
        this.entry = entry;
    }

    @Override
    public FriendResultType getType() {
        return FriendResultType.FriendRes_NotifyChange_FriendInfo;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(entry.getCharId());
        outPacket.encodeInt(entry.getAccountID());
        entry.encode(outPacket);
    }
}
