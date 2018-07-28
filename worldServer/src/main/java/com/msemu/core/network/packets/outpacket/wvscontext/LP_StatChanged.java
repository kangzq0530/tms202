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

package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.ExtendSP;
import com.msemu.world.client.character.NonCombatStatDayLimit;
import com.msemu.world.enums.Stat;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_StatChanged extends OutPacket<GameClient> {

    public LP_StatChanged() {
        this(new EnumMap<>(Stat.class), true, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0);
    }

    public LP_StatChanged(Map<Stat, Object> stats) {
        this(stats, true, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0);
    }

    public LP_StatChanged(Map<Stat, Object> stats, boolean exclRequestSent, byte mixBaseHairColor,
                          byte mixAddHairColor, byte mixHairBaseProb, byte charmOld, boolean updateCovery,
                          int hpRecovery, int mpRecovery) {
        super(OutHeader.LP_StatChanged);

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
                    encodeByte(((Integer) value).byteValue());
                    break;
                case JOB:
                    encodeInt((Integer) value);
                    break;
                case FACE:
                case HAIR:
                case HP:
                case MAX_HP:
                case MP:
                case MAX_MP:
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
                    encodeShort(((Integer) value).shortValue());
                    break;
                case SP:
                    if (value instanceof ExtendSP) {
                        ((ExtendSP) value).encode(this);
                    } else {
                        encodeShort(((Integer) value).shortValue());
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
