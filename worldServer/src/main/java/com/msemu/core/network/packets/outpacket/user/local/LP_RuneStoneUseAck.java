package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.runestones.results.IRuneResult;

public class LP_RuneStoneUseAck extends OutPacket<GameClient> {

    public LP_RuneStoneUseAck(IRuneResult result) {
        super(OutHeader.LP_RuneStoneUseAck);
        encodeInt(result.getType().getValue());
        result.encode(this);
    }
}
