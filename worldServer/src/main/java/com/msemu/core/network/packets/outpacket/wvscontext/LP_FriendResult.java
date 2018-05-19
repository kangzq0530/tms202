package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.friends.response.IFriendResponse;

/**
 * Created by Weber on 2018/5/11.
 */
public class LP_FriendResult extends OutPacket<GameClient> {

    public LP_FriendResult(IFriendResponse response) {
        super(OutHeader.LP_FriendResult);
        encodeByte(response.getType().getValue());
        response.encode(this);
    }
}
