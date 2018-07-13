package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_ModCombo extends OutPacket<GameClient> {

    public LP_ModCombo(int combo) {
        super(OutHeader.LP_ModCombo);
        encodeInt(combo);
    }
}