/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.skills.BurnedInfo;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_MobStatReset extends OutPacket<GameClient> {

    public LP_MobStatReset(Mob mob, byte byteCalcDamageStatIndex, boolean sn) {
        this(mob, byteCalcDamageStatIndex, sn, null);
    }

    public LP_MobStatReset(Mob mob, byte calcDamageStatIndex, boolean sn, List<BurnedInfo> burnedInfos) {
        super(OutHeader.LP_MobStatReset);
        MobTemporaryStat resetStats = mob.getTemporaryStat();
        int[] mask = resetStats.getRemovedMask();
        encodeInt(mob.getObjectId());
        Arrays.stream(mask).forEach(this::encodeInt);
        if (resetStats.hasRemovedMobStat(MobBuffStat.BurnedInfo)) {
            if (burnedInfos == null) {
                encodeInt(0);
                encodeInt(0);
            } else {
                int dotCount = burnedInfos.stream().mapToInt(BurnedInfo::getDotCount).sum();
                encodeInt(dotCount);
                encodeInt(burnedInfos.size());
                burnedInfos.forEach(info -> {
                    encodeInt(info.getCharacterId());
                    encodeInt(info.getSuperPos());
                    encodeInt(0);
                });
            }
            resetStats.getBurnedInfo().clear();
        }
        encodeByte(calcDamageStatIndex);
        if (resetStats.hasRemovedMovementAffectingStat()) {
            encodeByte(sn);
        }
        resetStats.getRemovedStatVals().clear();
    }
}
