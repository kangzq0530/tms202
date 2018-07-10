package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/6/2.
 */
public class LP_CorrectFootHoldMove extends OutPacket<GameClient> {

    public LP_CorrectFootHoldMove() {
        super(OutHeader.LP_CorrectFootHoldMove);
    }
}
