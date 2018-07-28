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

/**
 * Created by Weber on 2018/4/13.
 */
public enum DeathType {
    STAY(0),
    ANIMATION_DEATH(1),
    NO_ANIMATION_DEATH(2),
    INSTA_DEATH(3),
    NO_ANIMATION_DEATH_2(4),
    ANIMATION_DEATH_2(5),
    ANIMATION_DEATH_3(6),;
    private byte value;

    DeathType(byte val) {
        this.value = val;
    }

    DeathType(int val) {
        this((byte) val);
    }

    public static DeathType getByValue(int value) {
        for (DeathType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return ANIMATION_DEATH;
    }

    public byte getValue() {
        return value;
    }
}