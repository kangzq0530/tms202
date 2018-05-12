package com.msemu.world.client.character.friends;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
@Setter
public class FriendEntry {

    private int charId;
    private String friendName;
    private byte flag;
    private int channelId;
    private String friendGroup;
    private int accountID;
    private boolean mobile;
    private String friendNick;
    private String friendMemo;
    private boolean inShop;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(charId);
        outPacket.encodeString(friendName, 15);
        outPacket.encodeByte(flag);
        outPacket.encodeInt(channelId);
        outPacket.encodeString(friendGroup, 18);
        outPacket.encodeInt(accountID);
        outPacket.encodeByte(mobile);
        outPacket.encodeString(friendNick);
        outPacket.encodeString(friendName, 271);
        outPacket.encodeInt(inShop ? 1 : 0);
    }

}
