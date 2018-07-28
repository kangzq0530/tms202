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

import java.util.Arrays;

public enum RuneResultType {
    RRT_DATA_MISS_MATCH(0x0),
    RRT_PREOCCUPIED(0x1),
    // 還領取不到效果. (剩餘時間: %d秒)
    RRT_WAIT_TO_USE_RUNE(0x2),
    RRT_WAIT_TO_USE_RUNE2(0x3),
    // 因為輪的力量太強,再目前等級無法使用.
    RRT_UNDER_REQUIRED_LEVEL(0x5),
    // 魚餌的力量太強所以無法使用魚餌。
    RRT_UNDER_REQUIRED_LEVEL2(0x6),
    // 目前無法發動符文。
    RRT_CAN_NOT_USE(0x7),
    RRT_SUCCESS(0x8),
    RRT_UNKNOWN(0xFF);;
    @Getter
    private final int value;

    RuneResultType(int value) {
        this.value = value;
    }

    public static RuneResultType getByValue(final int value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue() == value)
                .findFirst().orElse(RRT_UNKNOWN);
    }

}
