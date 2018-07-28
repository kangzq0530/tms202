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

/**
 * Created by Weber on 2018/5/14.
 */
public enum EliteMobAttribute {

    堅固的(0x0),
    抗魔的(0x1),
    再生的(0x2),
    快速的(0x3),
    封印的(0x8),
    迴避的(0x9),
    虛弱的(0xA),
    昏倒的(0xB),
    詛咒的(0xC),
    猛毒的(0xD),
    黏黏的(0xE),
    噬魔的(0xF),
    魅惑的(0x10),
    毒霧的(0x13),
    混亂的(0x14),
    不死的(0x15),
    厭藥的(0x16),
    不停的(0x17),
    黑暗的(0x18),
    堅固的2(0x1E),
    反射的(0x21),
    無敵的(0x22),;

    private int value;

    private EliteMobAttribute(int value) {
        this.value = value;
    }

    public static EliteMobAttribute getRandomAttribute() {
        return EliteMobAttribute.values()[Rand.get(EliteMobAttribute.values().length)];
    }

    public int getValue() {
        return 0x70 + value;
    }

}
