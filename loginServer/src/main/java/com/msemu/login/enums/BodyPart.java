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

package com.msemu.login.enums;

/**
 * Created by Weber on 2018/4/21.
 */
public enum BodyPart {
    HAT(1),
    FACE(2),
    EYES(3),
    EARRINGS(4),
    TOP(5), // Includes overall
    PANTS(6),
    BOOTS(7),
    GLOVES(8),
    CAPE(9),
    SHIELD(10), // Includes things such as katara, 2ndary
    WEAPON(11),
    RING_1(12),
    RING_2(13),
    PET_EQUIP_1(14),
    RING_3(15),
    RING_4(16),
    NECKLACE(17),
    MOUNT(18),
    MOUNT_COVER(19),
    MEDAL(21),
    BELT(22),
    SHOULDER(23),
    PET_EQUIP_2(24),
    PET_EQUIP_3(25),
    POCKET(26),
    ANDROID(27),
    HEART_1(28),
    BADGE(29),
    EMBLEM(30),
    MECH_ENGINE(1100),
    MECH_ARM(1101),
    MECH_LEG(1102),
    MECH_FRAME(1103),
    MECH_TRANSISTOR(1104),
    NECKLACE_CASH(31),
    BITS(1400), // 1400~1424



    ;

    private int val;

    BodyPart(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
