package com.msemu.world.network.packets.MobPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.skills.MobTemporaryStat;

/**
 * Created by Weber on 2018/4/12.
 */
public class MobStatSet extends OutPacket {

    public MobStatSet(Mob mob, short delay) {
        super(OutHeader.MobStatSet);
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
