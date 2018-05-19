package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_MobStatSet extends OutPacket<GameClient> {

    public LP_MobStatSet(Mob mob, short delay) {
        super(OutHeader.LP_MobStatSet);
        MobTemporaryStat mts = mob.getTemporaryStat();
        boolean hasMovementStat = mts.hasNewMovementAffectingStat();
        encodeInt(mob.getObjectId());
        mts.encode(this);
        encodeShort(delay);
        encodeByte(1); // nCalcDamageStatIndex
        if (hasMovementStat) {
            encodeByte(0); // ?
        }

    }
}
