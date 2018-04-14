package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.skills.TemporaryStatManager;

/**
 * Created by Weber on 2018/4/11.
 */
public class TemporaryStateSet extends OutPacket {

    public TemporaryStateSet(TemporaryStatManager tsm) {
        super(OutHeader.TemporaryStatSet);
        boolean hasMovingAffectingStat = tsm.hasNewMovingEffectingStat(); // encoding flushes new stats
        tsm.encodeForLocal(this);
        encodeInt(0);
        encodeShort(1);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        if (hasMovingAffectingStat) {
            encodeByte(0);
        }
    }
}
