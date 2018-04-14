package com.msemu.world.network.packets.NpcPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Npc;

/**
 * Created by Weber on 2018/4/12.
 */
public class NpcChangeController extends OutPacket {


    public NpcChangeController(Npc npc, boolean controller) {
        super(OutHeader.NpcChangeController);
        encodeByte(controller);
        encodeInt(npc.getObjectId());
        if(!controller)
            return;
        encodeInt(npc.getTemplateId());
        npc.encode(this);
    }


}
