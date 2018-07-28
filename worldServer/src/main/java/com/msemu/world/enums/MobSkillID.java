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

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum MobSkillID {
    // Got these from Odin
    Unk(-1),
    PowerUp(100),
    MagicUp(101),
    PGuardUp(102),
    MGuardUp(103),
    ConsumeForHeal(105),
    PowerUp2(110),
    MagicUp2(111),
    PDDUp2(112),
    MDDUp2(113),
    Heal(114),
    Speed(115),
    Dispel(127),
    Banish(129),
    Mist(131),
    PImmune(140),
    MImmune(141),
    Immune(142),
    PReflect(143),
    MReflect(144),
    Reflect(145),
    PowerUp3(150),
    MagicUp3(151),
    PDDUp3(152),
    MDDUp3(153),
    Acc(154),
    Eva(155),
    Speed2(156),
    Seal(157),
    Tornado(173),
    Teleport(184),
    Summon(200),;

    private int value;

    MobSkillID(int value) {
        this.value = value;
    }

    public static MobSkillID getMobSkillIDByValue(int value) {
        return Arrays.stream(values()).filter(m -> m.getValue() == value).findFirst().orElse(Unk);
    }

    public int getValue() {
        return value;
    }
}
