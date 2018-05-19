package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_SetStandAloneMode extends OutPacket<GameClient> {
    public LP_SetStandAloneMode(boolean enable1, boolean enable2) {
        super(OutHeader.LP_SetStandAloneMode);
        encodeByte(enable1);
        if(enable1) {
            encodeShort(enable2 ? 1 : 0);
            encodeByte(0);
        } else {
            encodeByte(false);
        }
    }
}
