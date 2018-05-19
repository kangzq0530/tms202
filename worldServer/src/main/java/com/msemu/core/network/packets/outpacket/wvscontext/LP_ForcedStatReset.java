package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/25.
 */
public class LP_ForcedStatReset extends OutPacket<GameClient> {

    public LP_ForcedStatReset() {
        super(OutHeader.LP_ForcedStatReset);
    }
}
