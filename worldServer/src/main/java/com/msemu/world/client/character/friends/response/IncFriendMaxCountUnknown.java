package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class IncFriendMaxCountUnknown implements IFriendResponse {
    @Override
    public FriendResultType getType() {
        return FriendResultType.FriendRes_IncMaxCount_Unknown;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
