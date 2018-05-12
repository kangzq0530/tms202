package com.msemu.world.client.character.friends.response;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class InviteAccountFriend implements IFriendResponse {

    private int accountID;
    private String nickName;
    private int level;
    private int jobCode;
    private int subJobCode;

    public InviteAccountFriend(int accountID, String nickName, int level, int jobCode, int subJobCode) {
        this.accountID = accountID;
        this.nickName = nickName;
        this.level = level;
        this.jobCode = jobCode;
        this.subJobCode = subJobCode;
    }

    @Override
    public FriendResultType getType() {
        return FriendResultType.FriendRes_Invite;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

        outPacket.encodeByte(true);
        outPacket.encodeInt(0); // friendId, here is zero
        outPacket.encodeInt(accountID);
        outPacket.encodeInt(level);
        outPacket.encodeInt(jobCode);
        outPacket.encodeInt(subJobCode);

    }
}
