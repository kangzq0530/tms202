package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_FriendResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.friends.FriendList;
import com.msemu.world.client.character.friends.response.IncFriendMaxCountUnknown;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class CP_FriendRequest extends InPacket<GameClient> {

    private FriendResultType type;
    private int otherCharID;
    private String otherCharName;
    private String otherCharGroup;
    private String otherCharNickName;

    public CP_FriendRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        type = FriendResultType.getByValue(decodeByte());

        if (type == null)
            return;

        switch (type) {
            case FriendReq_DeleteFriend: {
                otherCharID = decodeInt();
                break;
            }
        }

    }

    @Override
    public void runImpl() {
        if (type == null)
            return;
        GameClient client = getClient();
        Character chr = client.getCharacter();
        FriendList friendList = chr.getFriendList();
        switch (type) {
            case FriendReq_SetFriend: {
                break;
            }
            case FriendReq_DeleteFriend: {
                break;
            }
            case FriendReq_IncMaxCount: {
                int maxCount = friendList.getMaxCount();
                if(maxCount >= 100 || chr.getMoney() < 50000) {
                    // TODO 訊息 沒有楓幣
                    return;
                }
                chr.addMoney(-50000);
                friendList.addMaxCount(5);
                client.write(new LP_FriendResult(new IncFriendMaxCountUnknown()));
                break;
            }
        }

    }
}
