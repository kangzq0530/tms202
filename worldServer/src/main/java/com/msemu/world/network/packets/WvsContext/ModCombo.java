package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/13.
 */
public class ModCombo extends OutPacket {

    public ModCombo(int combo) {
        super(OutHeader.ModCombo);
        encodeInt(combo);
    }
}
