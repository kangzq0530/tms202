package com.msemu.world.network.packets.WvsContext;

import com.msemu.world.client.character.ExtendSP;
import com.msemu.world.client.character.NonCombatStatDayLimit;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.Stat;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Weber on 2018/4/13.
 */
public class StatChanged extends OutPacket {

    public StatChanged(Map<Stat, Object> stats) {
        this(stats, true, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0);
    }

    public StatChanged(Map<Stat, Object> stats, boolean exclRequestSent, byte mixBaseHairColor,
                       byte mixAddHairColor, byte mixHairBaseProb, byte charmOld, boolean updateCovery,
                       int hpRecovery, int mpRecovery) {
        super(OutHeader.StatChanged);

        encodeByte(exclRequestSent);
        // GW_CharacterStat::DecodeChangeStat
        int mask = 0;
        for (Stat stat : stats.keySet()) {
            mask |= stat.getValue();
        }
        encodeLong(mask);
        Comparator<Stat> statComparator = Comparator.comparingLong(Stat::getValue);
        TreeMap<Stat, Object> sortedStats = new TreeMap<>(statComparator);
        sortedStats.putAll(stats);
        for (Map.Entry<Stat, Object> entry : sortedStats.entrySet()) {
            Stat stat = entry.getKey();
            Object value = entry.getValue();
            switch (stat) {
                case SKIN:
                case LEVEL:
                case FATIGUE:
                case ICE_GAGE:
                    encodeByte((Byte) value);
                    break;
                case JOB:
                    encodeInt((Integer) value);
                    break;
                case FACE:
                case HAIR:
                case HP:
                case MAXHP:
                case MP:
                case MAXMP:
                case POP:
                case CHARISMA:
                case INSIGHT:
                case WILL:
                case CRAFT:
                case SENSE:
                case CHARM:
                case VIRTUE:
                    encodeInt((Integer) value);
                    break;
                case STR:
                case DEX:
                case INT:
                case LUK:
                case AP:
                    encodeShort((Short) value);
                    break;
                case SP:
                    if (value instanceof ExtendSP) {
                        ((ExtendSP) value).encode(this);
                    } else {
                        encodeShort((Short) value);
                    }
                    break;
                case EXP:
                case MONEY:
                    encodeLong((Long) value);
                    break;
                case DAY_LIMIT:
                    ((NonCombatStatDayLimit) value).encode(this);
                    break;
            }
        }

        encodeByte(mixBaseHairColor);
        encodeByte(mixAddHairColor);
        encodeByte(mixHairBaseProb);
        encodeByte(charmOld > 0);
        if (charmOld > 0) {
            encodeByte(charmOld);
        }
        encodeByte(updateCovery);
        if (updateCovery) {
            encodeInt(hpRecovery);
            encodeInt(mpRecovery);
        }
    }
}
