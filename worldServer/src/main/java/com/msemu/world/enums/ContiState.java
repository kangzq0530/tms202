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

/**
 * Created by Weber on 2018/6/1.
 */
public enum ContiState {
    CONTI_DORMANT(0),
    CONTI_WAIT(1),
    CONTI_START(2),
    CONTI_MOVE(3),
    CONTI_MOB_GEN(4),
    CONTI_MOB_DESTROY(5),
    CONTI_END(6),
    CONTI_TARGET_START_FIELD(7),
    CONTI_TARGET_START_SHIP_MOVE_FIELD(8),
    CONTI_TARGET_MOVE_FIELD(10),
    CONTI_TARGET_END_SHIP_MOVE_FIELD(12),;

    @Getter
    private int value;

    ContiState(int value) {
        this.value = value;
    }
}
