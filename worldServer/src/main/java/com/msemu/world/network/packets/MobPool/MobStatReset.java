package com.msemu.world.network.packets.MobPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.skills.BurnedInfo;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.enums.MobStat;

import java.util.List;

/**
 * Created by Weber on 2018/4/12.
 */
public class MobStatReset extends OutPacket {

    public MobStatReset(Mob mob, byte byteCalcDamageStatIndex, boolean sn) {
        this(mob, byteCalcDamageStatIndex, sn, null);
    }

    public MobStatReset(Mob mob, byte calcDamageStatIndex, boolean sn, List<BurnedInfo> biList) {
        super(OutHeader.MobStatReset);
        MobTemporaryStat resetStats = mob.getTemporaryStat();
        int[] mask = resetStats.getRemovedMask();
        encodeInt(mob.getObjectId());
        for (int i = 0; i < 3; i++) {
            encodeInt(mask[i]);
        }
        if(resetStats.hasRemovedMobStat(MobStat.BurnedInfo)) {
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
            resetStats.getBurnedInfos().clear();
        }
        encodeByte(calcDamageStatIndex);
        if(resetStats.hasRemovedMovementAffectingStat()) {
            encodeByte(sn);
        }
        resetStats.getRemovedStatVals().clear();
    }
}
