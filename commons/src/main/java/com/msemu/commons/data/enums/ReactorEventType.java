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

import lombok.Getter;

public enum ReactorEventType {
    REACTOR_EVENT_HIT(0x0),
    REACTOR_EVENT_HIT_LEFT(0x1),
    REACTOR_EVENT_HIT_RIGHT(0x2),
    REACTOR_EVENT_HIT_JUMP_LEFT(0x3),
    REACTOR_EVENT_HIT_JUMP_RIGHT(0x4),
    REACTOR_EVENT_HIT_SKILL_CHECK(0x5),
    REACTOR_EVENT_GATHER(0x8),
    REACTOR_EVENT_CLICK_CHECK(0x9),
    REACTOR_EVENT_MOB_CHECK(0xA),
    REACTOR_EVENT_CHARACTER_ACT(0xB),
    REACTOR_EVENT_REMOVE_REACTOR(0xC),
    REACTOR_EVENT_HIT_JUMP(0xD),
    REACTOR_EVENT_FIND_ITEM_UPDATE(0x64),
    REACTOR_EVENT_TIMEOUT_RESET(0x65),
    REACTOR_EVENT_KEY_CHECK1(0xC8),
    REACTOR_EVENT_KEY_CHECK2(0xC9),
    NONE(-1);
    @Getter
    private final int value;

    ReactorEventType(int value) {
        this.value = value;
    }

    public static ReactorEventType getByValue(int value) {
        for (ReactorEventType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return NONE;
    }
}
