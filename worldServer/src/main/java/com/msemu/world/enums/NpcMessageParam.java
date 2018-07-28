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
 * Created by Weber on 2018/5/15.
 */
public enum NpcMessageParam {

    NONE(0),
    NO_CLOSE(1),
    SHOW_CHAR_ON_RIGHT_SIDE(2),
    NPC_ON_LEFT_SIDE_OVERRIDE(4),
    NPC_ON_LEFT_SIDE_TURN_RIGHT(8),
    CHAR_ON_RIGHT_SIDE_TURN_RIGHT(16),
    BOTTOM_DIALOG_WITH_DELAY(32),;
    @Getter
    private int value;

    NpcMessageParam(int value) {
        this.value = value;
    }
}
