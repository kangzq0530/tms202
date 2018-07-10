package com.msemu.core.network.packets.outpacket.field.types.conti;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ContiState;

/**
 * Created by Weber on 2018/6/1.
 */
public class LP_ContiMove extends OutPacket<GameClient> {

    public LP_ContiMove(ContiState state) {
        this.encodeByte(state.getValue());
    }
}
