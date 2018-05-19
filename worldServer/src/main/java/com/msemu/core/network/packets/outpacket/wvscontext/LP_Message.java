package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.messages.IWvsMessage;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_Message extends OutPacket<GameClient> {

    public LP_Message(IWvsMessage message) {
        super(OutHeader.LP_Message);
        encodeByte(message.getType().getValue());
        message.encode(this);
    }
}
