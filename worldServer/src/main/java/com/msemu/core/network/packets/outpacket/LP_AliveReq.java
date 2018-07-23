package com.msemu.core.network.packets.outpacket;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

public class LP_AliveReq extends OutPacket<GameClient> {

    public LP_AliveReq() {
        super(OutHeader.LP_AliveReq);
    }
}
