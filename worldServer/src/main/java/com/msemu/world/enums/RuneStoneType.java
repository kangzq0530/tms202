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

import com.msemu.commons.utils.Rand;
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
    RST_COUNT(0x9),;
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
