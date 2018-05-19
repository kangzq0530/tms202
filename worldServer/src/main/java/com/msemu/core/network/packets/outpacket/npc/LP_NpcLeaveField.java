package com.msemu.core.network.packets.outpacket.npc;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Npc;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_NpcLeaveField extends OutPacket<GameClient> {
    public LP_NpcLeaveField(Npc npc) {
        super(OutHeader.LP_NpcLeaveField);
        encodeInt(npc.getObjectId());
    }
}
