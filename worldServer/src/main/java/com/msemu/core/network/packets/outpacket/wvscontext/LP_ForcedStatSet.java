package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ForcedStat;

import java.util.Map;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_ForcedStatSet extends OutPacket<GameClient> {

    public LP_ForcedStatSet(Map<ForcedStat, Integer> forcedStats) {
        super(OutHeader.LP_ForcedStatSet);
        int mask = 0;
        for(ForcedStat forcedStat : forcedStats.keySet())
            mask |=  forcedStat.getValue();
        encodeInt(mask);
        for(Map.Entry<ForcedStat, Integer> entry : forcedStats.entrySet()) {
            switch (entry.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
                    encodeByte(entry.getValue());
                    break;
                default:
                    encodeShort(entry.getValue());
                    break;
            }
        }
    }
}
