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

public enum MiniRoomLeaveResult {
    MR_UserRequest(0x0),
    MR_WrongPosition(0x1),
    MR_Closed(0x2),
    MR_HostOut(0x3),
    MR_Booked(0x4),
    MR_Kicked(0x5),
    MR_OpenTimeOver(0x6),
    TR_TradeDone(0x7),
    TR_TradeFail(0x8),
    TR_TradeFail_OnlyItem(0x9),
    TR_TradeFail_Expired(0xA),
    TR_TradeFail_Denied(0xB),
    TR_TradeFail_Invalid_CashItemSN(0xC),
    TR_FieldError(0xD),
    TR_ItemCRCFailed(0xE),
    TR_Trade_UnableWorld(0xF),
    PS_NoMoreItem(0x10),
    PS_KickedTimeOver(0x11),
    ES_Open(0x12),
    ES_StartManage(0x13),
    ES_ClosedTimeOver(0x14),
    ES_EndManage(0x15),
    ES_DestroyByAdmin(0x16),
    MG_UserRequest(0x17),
    WR_PartnerGone(0x18),
    WR_CanceledByNoInventory0(0x19),
    WR_CanceledByNoInventory1(0x1A),
    WR_GracefulClose(0x1B),
    CMR_NoCandy(0x1C),
    CMR_NoHostInven(0x1D),
    CMR_NoGuestInven(0x1E),
    CMR_Reject(0x1F),
    CMR_NotOnTime(0x20),
    CMR_Done(0x21),
    CMR_Failed(0x22),;
    @Getter
    private final int value;

    MiniRoomLeaveResult(int value) {
        this.value = value;
    }
}
