package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.AffectedArea;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_AffectedAreaRemoved extends OutPacket<GameClient> {

    public LP_AffectedAreaRemoved(AffectedArea aa, boolean mistEruption) {
        super(OutHeader.LP_AffectedAreaRemoved);

        encodeInt(aa.getObjectId());
        if(aa.getSkillID() == 2111003) {
            encodeByte(mistEruption);
        }
    }
}
