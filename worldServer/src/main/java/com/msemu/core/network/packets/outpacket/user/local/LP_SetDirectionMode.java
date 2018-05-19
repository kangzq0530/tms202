package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_SetDirectionMode extends OutPacket<GameClient> {

    public LP_SetDirectionMode(boolean enable) {
        super(OutHeader.LP_SetDirectionMode);
        encodeInt(enable ? 1 : 0);
        encodeInt(0);
    }
}
