package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.runestones.RuneStone;
import com.msemu.world.enums.RuneStoneType;

public class LP_RuneStoneSkillAck extends OutPacket<GameClient> {
    public LP_RuneStoneSkillAck(RuneStone runeStone) {
        super(OutHeader.LP_RuneStoneSkillAck);
        encodeInt(runeStone.getType().getValue());
    }
}
