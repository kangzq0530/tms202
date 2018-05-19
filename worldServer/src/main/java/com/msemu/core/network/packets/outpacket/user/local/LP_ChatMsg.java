package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.ChatMsgType;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_ChatMsg extends OutPacket {

    public LP_ChatMsg(ChatMsgType color, String message) {
        super(OutHeader.LP_UserChatMsg);
        encodeShort(color.getValue());
        encodeString(message);
    }
}
