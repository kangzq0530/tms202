package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.runestones.RuneStone;

public class LP_RuneStoneDisappear extends OutPacket<GameClient> {
    public LP_RuneStoneDisappear(RuneStone runeStone, Character chr, boolean destroyed) {
        super(OutHeader.LP_RuneStoneDisappear);
        encodeInt(runeStone.getIndex());
        encodeInt(chr != null ? chr.getId() : 0);
        encodeInt(0); // unknown
        encodeByte(destroyed);
    }
}
