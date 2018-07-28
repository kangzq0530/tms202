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

package com.msemu.world.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/12.
 */
public enum Stat {
    SKIN(0x1), // byte
    FACE(0x2), // int
    HAIR(0x4), // int
    LEVEL(0x10), // byte
    JOB(0x20), // int ( job + subjob )
    STR(0x40), // short
    DEX(0x80), // short
    INT(0x100), // short
    LUK(0x200), // short
    HP(0x400), // int
    MAX_HP(0x800), // int
    MP(0x1000), // int
    MAX_MP(0x2000), // int
    AP(0x4000), // short
    SP(0x8000), // short (depends)
    EXP(0x10000), // long
    POP(0x20000), // int
    MONEY(0x40000), // long
    PET(0x180008), // Pets: 0x8 + 0x80000 + 0x100000  [3 longs]
    FATIGUE(0x80000), // byte
    CHARISMA(0x100000), // ambition int
    INSIGHT(0x200000),
    WILL(0x400000), // int
    CRAFT(0x800000), // dilligence, int
    SENSE(0x1000000), // empathy, int
    CHARM(0x2000000), // int
    DAY_LIMIT(0x4000000), // 21 bytes
    BATTLE_EXP(0x8000000), // int
    BATTLE_RANK(0x10000000), // byte
    BATTLE_POINTS(0x20000000),
    ICE_GAGE(0x40000000), // byte byte
    VIRTUE(0x80000000), // int
    GACHAPON_EXP(0x100000000L), // long
    GENDER(0x200000000L); // int
    ;


    private long val;

    Stat(long val) {
        this.val = val;
    }

    public static Stat getByVal(long stat) {
        return Arrays.stream(values()).filter(s -> s.getValue() == stat).findFirst().orElse(null);
    }

    public static List<Stat> getStatsByFlag(int mask) {
        List<Stat> stats = new ArrayList<>();
        List<Stat> allStats = Arrays.asList(values());
        Collections.sort(allStats);
        stats.addAll(allStats.stream().filter(stat -> (stat.getValue() & mask) != 0).collect(Collectors.toList()));
        return stats;
    }

    public long getValue() {
        return val;
    }
}
