package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.skills.BurnedInfo;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;

import java.util.List;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_MobStatReset extends OutPacket<GameClient> {

    public LP_MobStatReset(Mob mob, byte byteCalcDamageStatIndex, boolean sn) {
        this(mob, byteCalcDamageStatIndex, sn, null);
    }

    public LP_MobStatReset(Mob mob, byte calcDamageStatIndex, boolean sn, List<BurnedInfo> biList) {
        super(OutHeader.LP_MobStatReset);
        MobTemporaryStat resetStats = mob.getTemporaryStat();
        int[] mask = resetStats.getRemovedMask();
        encodeInt(mob.getObjectId());
        for (int i = 0; i < 3; i++) {
            encodeInt(mask[i]);
        }
        if(resetStats.hasRemovedMobStat(MobBuffStat.BurnedInfo)) {
            if(biList == null) {
                encodeInt(0);
                encodeInt(0);
            } else {
                int dotCount = biList.stream().mapToInt(BurnedInfo::getDotCount).sum();
                encodeInt(dotCount);
                encodeInt(biList.size());
                for(BurnedInfo bi : biList) {
                    encodeInt(bi.getCharacterId());
                    encodeInt(bi.getSuperPos());
                }
            }
            resetStats.getBurnedInfo().clear();
        }
        encodeByte(calcDamageStatIndex);
        if(resetStats.hasRemovedMovementAffectingStat()) {
            encodeByte(sn);
        }
        resetStats.getRemovedStatVals().clear();
    }
}
