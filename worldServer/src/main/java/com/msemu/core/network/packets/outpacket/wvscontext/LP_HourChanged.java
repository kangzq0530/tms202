package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_HourChanged extends OutPacket<GameClient> {
    public LP_HourChanged(int hour) {
        super(OutHeader.LP_HourChanged);
        encodeInt(hour);
        encodeShort(1);
    }
}
