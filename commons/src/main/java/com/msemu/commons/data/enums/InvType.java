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

package com.msemu.commons.data.enums;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/12.
 */
public enum InvType {

    EQUIPPED(-1),
    EQUIP(1),
    CONSUME(2),
    INSTALL(3),
    ETC(4),
    CASH(5),
    TRADE(6),;

    private byte value;

    InvType(final int value) {
        this((byte) value);
    }

    InvType(byte value) {
        this.value = value;
    }

    public static InvType getInvTypeByValue(int val) {
        return Arrays.stream(InvType.values()).filter(t -> t.getValue() == val).findFirst().orElse(null);
    }

    public static InvType getInvTypeByString(String subMap) {
        InvType res = null;
        switch (subMap.toLowerCase()) {
            case "cash":
            case "pet":
                res = CASH;
                break;
            case "consume":
            case "special":
            case "use":
                res = CONSUME;
                break;
            case "etc":
                res = ETC;
                break;
            case "install":
            case "setup":
                res = INSTALL;
                break;
        }
        return res;
    }

    public byte getValue() {
        return value;
    }

    public boolean isStackable() {
        return this != EQUIP && this != EQUIPPED;
    }
}
