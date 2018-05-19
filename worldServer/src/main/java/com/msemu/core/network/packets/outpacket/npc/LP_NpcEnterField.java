package com.msemu.core.network.packets.outpacket.npc;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Npc;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_NpcEnterField extends OutPacket<GameClient> {

    public LP_NpcEnterField(Npc npc) {
        super(OutHeader.LP_NpcEnterField);
        encodeInt(npc.getObjectId());
        encodeInt(npc.getTemplateId());
        npc.encode(this);
    }
}
