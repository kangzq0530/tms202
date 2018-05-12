package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class StarFriendSetFriendMaster implements IFriendResponse {
    @Override
    public FriendResultType getType() {
        return FriendResultType.StarFriend_Res_SetFriend_Master;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
