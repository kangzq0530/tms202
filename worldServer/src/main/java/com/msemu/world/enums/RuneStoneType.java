package com.msemu.world.enums;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.utils.Rand;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import lombok.Getter;

import java.util.Arrays;

public enum RuneStoneType {
    RST_NONE(0xFFFFFFFF),
    RST_UPGRADE_SPEED(0x0),
    RST_UPGRADE_DEFENCE(0x1),
    RST_DOT_ATTACK(0x2),
    RST_THUNDER(0x3),
    RST_EARTHQUAKE(0x4),
    RST_SUMMON_ELITE_MOB(0x5),
    RST_SUMMON_MIMIC(0x6),
    RST_INCREASE(0x7),
    RST_REDUCE_COOL_TIME(0x8),
    RST_COUNT(0x9),
    ;
    @Getter
    private final int value;


    RuneStoneType(int value) {
        this.value = value;
    }

    public static RuneStoneType randomType() {
        return values()[Rand.get(values().length)];
    }

    public static RuneStoneType getByValue(final int value) {
        return Arrays.stream(values()).filter(type -> type.getValue() == value)
                .findFirst().orElse(RST_NONE);
    }
}
