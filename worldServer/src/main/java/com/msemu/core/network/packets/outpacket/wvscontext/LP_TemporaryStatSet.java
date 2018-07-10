package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.stats.TemporaryStatManager;

/**
 * Created by Weber on 2018/4/11.
 */
public class LP_TemporaryStatSet extends OutPacket<GameClient> {


    public LP_TemporaryStatSet(TemporaryStatManager tsm) {
        super(OutHeader.LP_TemporaryStatSet);
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
