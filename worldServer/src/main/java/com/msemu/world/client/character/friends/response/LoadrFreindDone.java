package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.friends.FriendEntry;
import com.msemu.world.enums.FriendResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
public class LoadrFreindDone implements IFriendResponse {

    private List<FriendEntry> friends = new ArrayList<>();

    public LoadrFreindDone(List<FriendEntry> friends) {
        this.friends = friends;
    }

    @Override
    public FriendResultType getType() {
        return FriendResultType.FriendRes_LoadFriend_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(friends.size());

    }
}
