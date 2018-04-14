package com.msemu.world.network.packets.NpcPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Npc;

/**
 * Created by Weber on 2018/4/12.
 */
public class NpcEnterField extends OutPacket {

    public NpcEnterField(Npc npc) {
        super(OutHeader.NpcEnterField);
        encodeInt(npc.getObjectId());
        encodeInt(npc.getTemplateId());
        npc.encode(this);
    }
}
