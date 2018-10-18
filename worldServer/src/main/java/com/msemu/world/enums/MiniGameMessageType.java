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

public enum MiniGameMessageType {
    MiniGame_UserBan(0x0C),
    MiniGame_UserTurn(0x0D),
    MiniGame_UserGiveUp(0x0E),
    MiniGame_UserRetreatSuccess(0x0F),
    MiniGame_UserLeave(0x10),
    MiniGame_UserLeaveEngage(0x11),
    MiniGame_UserLeaveEngageCancel(0x12),
    MiniGame_UserEnter(0x13),
    MiniGame_UserNotEnoughMoney(0x14),
    MiniGame_UserMatchCard(0x15),
    MiniGame_10SecAlert(0x71),
    MiniGame_GameStart(0x72),
    MiniGame_GameEnd(0x73),

    ;
    @Getter
    private int value;

    MiniGameMessageType(int value) {
        this.value = value;
    }
}
