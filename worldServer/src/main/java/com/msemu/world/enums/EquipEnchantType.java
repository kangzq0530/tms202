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

import lombok.Getter;

public enum EquipEnchantType {

    SCROLL_UPGRADE(0x0),
    HYPER_UPGRADE(0x1),
    TRANSMISSION(0x2),
    POTENTIAL_UPGRADE(0x3),
    ADDITIONAL_POTENTIAL_UPGRADE(0x4),
    EXTENDED_UPGRADE(0x5),
    SOUL_WEAPON_UPGRADE(0x6),
    NO_TYPES(0x7),
    DISPLAY_SCROLL_UPGRADE(0x32),
    DISPLAY_SCROLL_UPDATE(0x33),
    DISPLAY_HYPER_UPGRADE(0x34),
    DISPLAY_MINI_GAME(0x35),
    DISPLAY_SCROLL_UPGRADE_RESULT(0x64),
    DISPLAY_HYPER_UPGRADE_RESULT(0x65),
    DISPLAY_SCROLL_VESTIGE_COMPENSATION_RESULT(0x66),
    DISPLAY_TRANSMISSION_RESULT(0x67),
    DISPLAY_UNKNOWN_FAIL_RESULT(0x68),
    DISPLAY_BLOCK(0x69),
    NONE(-1);
    @Getter
    private final int value;

    EquipEnchantType(int value) {
        this.value = value;
    }

    public static EquipEnchantType getByValue(int value) {
        for (EquipEnchantType type : values()) {
            if (type.getValue() == value)
                return type;
        }
        return NONE;
    }


}
