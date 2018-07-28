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
 * Created by Weber on 2018/4/11.
 */

public enum PortalType {
    // http://mapleref.wikia.com/wiki/Portal
    StartPoint(0),
    PortalInvisible(1),
    PortalVisible(2),
    PortalCollision(3),
    PortalChangable(4),
    PortalChangableInvisible(5),
    TownPortalPoint(6),
    PortalScript(7),
    PortalScriptInvisible(8),
    PortalCollisionScript(9),
    PortalHidden(10),
    PortalScriptHidden(11),
    PortalCollisionJump(12),
    PortalCollisionCustom(13),
    PortalCollisionInvisibleChangable(14), // ?
    PortalUnk(15), // ? On map ID 910800200
    ;
    private byte value;

    PortalType(byte val) {
        this.value = val;
    }

    PortalType(int val) {
        this((byte) val);
    }

    public static PortalType getTypeByInt(int i) {
        return Arrays.stream(values()).filter(p -> p.getValue() == i).findFirst().orElse(null);
    }

    public byte getValue() {
        return value;
    }
}
