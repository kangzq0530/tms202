package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/11.
 */
public class CP_UserAbilityMassUpRequest extends InPacket<GameClient> {

    private int updateTick;
    private int autoAPCount;
    private long primaryStatVal;
    private int primaryStatAmount;
    private long secondaryStatVal;
    private int secondaryStatAmount;


    public CP_UserAbilityMassUpRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        autoAPCount = decodeInt();
        if (available() < autoAPCount * 12)
            return;
        primaryStatVal = decodeLong();
        primaryStatAmount = decodeInt();
        if (autoAPCount > 1) {
            secondaryStatVal = decodeLong();
            secondaryStatAmount = decodeInt();
        } else {
            secondaryStatVal = 0;
            secondaryStatAmount = 0;
        }
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();

        int requiredAp = primaryStatAmount + secondaryStatAmount;

        if (chr.getStat(Stat.AP) < requiredAp)
            return;

        Stat primaryStat = Stat.getByVal(primaryStatVal);

        Map<Stat, Integer> stats = new EnumMap<>(Stat.class);

        stats.put(primaryStat, primaryStatAmount);

        if (autoAPCount > 1) {
            Stat secondaryStat = Stat.getByVal(secondaryStatVal);
            if (secondaryStat != null)
                stats.put(secondaryStat, secondaryStatAmount);
        }
        chr.addStat(Stat.AP, -(primaryStatAmount + secondaryStatAmount));
        chr.addStat(stats);
    }
}
