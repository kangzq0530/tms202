package com.msemu.core.network.packets.outpacket.user.common;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ChatMsgType;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_UserChat extends OutPacket<GameClient> {

    public LP_UserChat(ChatMsgType color, String message) {
        super(OutHeader.LP_UserChat);
        encodeShort(color.getValue());
        encodeString(message);
    }
}
