package com.msemu.core.network.packets.outpacket.socket;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

public class LP_AuthenCodeChanged extends OutPacket<GameClient> {

    public LP_AuthenCodeChanged() {
        super(OutHeader.LP_AuthenCodeChanged);
        encodeByte(0);
        encodeInt(0);
    }
}
