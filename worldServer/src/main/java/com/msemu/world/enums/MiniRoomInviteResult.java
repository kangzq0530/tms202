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

public enum MiniRoomInviteResult {
    Success(0x0),
    NoCharacter(0x1),
    CannotInvite(0x2),
    Rejected(0x3),
    Blocked(0x4),
    CantInvite_DupeCharacter(0x5),
    OverPlay(0x6),
    CantInvite_OverPlay(0x7),
    LowPop(0x8),
    CantInvite_LowPop(0x9),
    NotSameField(0xA),
    Busy(0xB),
    Limit(0xC),
    Invalid_Map(0xD),
    BlockedBehavior(0xE),
    None(-1);
    ;
    @Getter
    private final int value;

    MiniRoomInviteResult(int value) {
        this.value = value;
    }

    public static MiniRoomInviteResult getByValue(int value) {
        for(MiniRoomInviteResult type: values()) {
            if(type.getValue() == value) {
                return type;
            }
        }
        return None;
    }
}
