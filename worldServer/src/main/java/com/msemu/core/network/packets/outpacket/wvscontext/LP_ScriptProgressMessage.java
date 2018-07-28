package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

public class LP_ScriptProgressMessage extends OutPacket<GameClient> {

    public LP_ScriptProgressMessage(String text) {
        super(OutHeader.LP_ScriptProgressMessage);
        encodeString(text);
    }
}
