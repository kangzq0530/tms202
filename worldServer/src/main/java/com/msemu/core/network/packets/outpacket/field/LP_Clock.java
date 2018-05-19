package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.clock.IClock;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_Clock extends OutPacket<GameClient> {

    public LP_Clock(IClock clock) {
        super((OutHeader.LP_Clock));
        encodeByte(clock.getType().getValue());
        clock.encode(this);
    }
}
