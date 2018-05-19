package com.msemu.core.network.packets.outpacket.npc;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Npc;

/**
 * Created by Weber on 2018/5/2.
 */
public class LP_NpcUpdateLimitedInfo extends OutPacket<GameClient> {

    public LP_NpcUpdateLimitedInfo(Npc npc, int value, int x, int y) {
        super(OutHeader.LP_NpcUpdateLimitedInfo);
        encodeInt(npc.getObjectId());
        encodeInt(value);
        encodeInt(x);
        encodeInt(y);
    }
}
