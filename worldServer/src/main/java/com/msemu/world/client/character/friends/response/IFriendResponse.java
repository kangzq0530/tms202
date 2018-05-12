package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/7.
 */
public interface IFriendResponse {

    FriendResultType getType();

    void encode(OutPacket<GameClient> outPacket);
}
