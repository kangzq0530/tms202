package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.QuickMoveNpcInfo;

import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_SetQuickMoveInfo extends OutPacket<GameClient> {

    public LP_SetQuickMoveInfo(List<QuickMoveNpcInfo> quickMoveNpcs) {
        super(OutHeader.LP_SetQuickMoveInfo);
        encodeByte(quickMoveNpcs.size());
        quickMoveNpcs.forEach(npc -> {
            encodeInt(quickMoveNpcs.indexOf(npc));
            encodeInt(npc.getId());
            encodeInt(npc.getType());
            encodeInt(npc.getLevel());
            encodeString(npc.getDesc());
            npc.getStart().encode(this);
            npc.getEnd().encode(this);
        });
    }
}
