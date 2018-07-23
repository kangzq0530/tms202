package com.msemu.world.enums;

import com.msemu.commons.utils.Rand;
import lombok.Getter;

public enum RuneStoneType {
    RST_NONE(0xFFFFFFFF, 0),
    RST_UPGRADE_SPEED(0x0, 80001427),
    RST_UPGRADE_DEFENCE(0x1, 80001428),
    RST_DOT_ATTACK(0x2, 80001429),
    RST_THUNDER(0x3, 80001756),
    RST_EARTHQUAKE(0x4, 80001757),
    RST_SUMMON_ELITE_MOB(0x5, 80001754),
    RST_SUMMON_MIMIC(0x6, 80001433),
    RST_INCREASE(0x7, 0),
    RST_REDUCE_COOL_TIME(0x8, 0),
    RST_COUNT(0x9, 0),;
    @Getter
    private final int value;

    @Getter
    private final int skill;

    RuneStoneType(int value, int skill) {
        this.value = value;
        this.skill = skill;
    }

    public static RuneStoneType randomType() {
        return values()[Rand.get(values().length)];
    }
}
