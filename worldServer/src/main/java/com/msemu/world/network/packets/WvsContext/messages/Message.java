package com.msemu.world.network.packets.WvsContext.messages;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/13.
 */
public class Message extends OutPacket {

    public Message() {
        super(OutHeader.Message);
    }
}
