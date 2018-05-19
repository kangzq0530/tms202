package com.msemu.core.network.packets.outpacket.npc;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Npc;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_NpcChangeController extends OutPacket<GameClient> {
    public LP_NpcChangeController(Npc npc, boolean controller) {
        super(OutHeader.LP_NpcChangeController);
        encodeByte(controller);
        encodeInt(npc.getObjectId());
        if(!controller)
            return;
        encodeInt(npc.getTemplateId());
        npc.encode(this);
    }
}
