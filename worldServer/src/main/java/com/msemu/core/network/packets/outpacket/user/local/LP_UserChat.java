package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/5.
 */
public class LP_UserChat extends OutPacket<GameClient> {
    public LP_UserChat(int sourceCharacterID, String text, byte type) {
        this(sourceCharacterID, false, text, type, -1);
    }

    public LP_UserChat(int sourceCharacterID, boolean whiteBG, String text, byte type) {
        this(sourceCharacterID, whiteBG, text, type, -1);
    }


    public LP_UserChat(int sourceCharacterID, boolean whiteBG, String text
            , byte type, int worldID) {
        super(OutHeader.LP_UserChat);
        encodeInt(sourceCharacterID);
        encodeByte(whiteBG);
        encodeString(text);
        encodeByte(type);
        encodeByte(false);
        encodeByte(worldID);
    }
}
