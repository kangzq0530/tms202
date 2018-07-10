package com.msemu.core.network.packets.outpacket.field.types.conti;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ContiState;

/**
 * Created by Weber on 2018/6/1.
 */
public class LP_ContiState extends OutPacket<GameClient> {

    public LP_ContiState(ContiState state, boolean appeared) {
        super(OutHeader.LP_ContiState);

        encodeByte(state.getValue());
        encodeByte(appeared);

    }
}
