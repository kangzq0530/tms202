package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.runestones.RuneStone;

import java.util.List;

public class LP_RuneStoneClearAndAllRegister extends OutPacket<GameClient> {

    public LP_RuneStoneClearAndAllRegister(List<RuneStone> runeStones) {
        super(OutHeader.LP_RuneStoneClearAndAllRegister);
        encodeInt(runeStones.size());
        encodeInt(0);
        runeStones.forEach(runeStone -> {
            runeStone.encode(this);
        });
    }
}
