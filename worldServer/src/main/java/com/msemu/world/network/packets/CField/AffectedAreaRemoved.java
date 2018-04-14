package com.msemu.world.network.packets.CField;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.AffectedArea;

/**
 * Created by Weber on 2018/4/13.
 */
public class AffectedAreaRemoved extends OutPacket {

    public AffectedAreaRemoved(AffectedArea aa, boolean mistEruption) {
        super(OutHeader.AffectedAreaRemoved);

        encodeInt(aa.getObjectId());
        if(aa.getSkillID() == 2111003) {
            encodeByte(mistEruption);
        }
    }
}
