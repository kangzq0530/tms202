package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.runestones.RuneStone;

public class LP_RuneStoneAppear extends OutPacket<GameClient> {

    public LP_RuneStoneAppear(RuneStone runeStone) {
        super(OutHeader.LP_RuneStoneAppear);
        encodeInt(runeStone.getIndex());
        encodeInt(0);
        runeStone.encode(this);
    }


}
