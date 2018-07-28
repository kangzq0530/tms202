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

public enum PickUpMessageType {
    ITEM_SN_DUPLICATE_FAIL(-6),
    OWNERSHIP_FAIL_OTHER_ITEM(-5),
    OWNERSHIP_FAIL(-4),
    ITEM_CRC_FAIL(-3),
    MAX_COUNT_FAIL(-2),
    ITEM_FULL_FAIL(-1),
    ADD_INVENTORY_ITEM(0x0),
    MESSO(0x1),
    CONSUME(0x2),
    DONE(0x3),
    NO_MSG(0x4),
    MESSO_TO_BOAT(8);
    @Getter
    private final int value;

    PickUpMessageType(int value) {
        this.value = value;
    }

}
