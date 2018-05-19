package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_InGameCurNodeEventEnd extends OutPacket<GameClient> {

    public LP_InGameCurNodeEventEnd(boolean enable) {
        super(OutHeader.LP_InGameCurNodeEventEnd);
        encodeByte(enable);
    }
}
