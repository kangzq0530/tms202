package com.msemu.world.network.packets.UserPool.UserLocal;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.ChatMsgColor;

/**
 * Created by Weber on 2018/4/13.
 */
public class ChatMsg extends OutPacket {

    public ChatMsg(ChatMsgColor color, String message) {
        super(OutHeader.UserChatMsg);
        encodeShort(color.getValue());
        encodeString(message);
    }
}
