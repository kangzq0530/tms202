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

public enum MiniRoomEnterResult {
    MR_Success(0x0),
    MR_NoRoom(0x1),
    MR_Full(0x2),
    MR_Busy(0x3),
    MR_Dead(0x4),
    MR_Event(0x5),
    MR_PermissionDenied(0x6),
    MR_NoTrading(0x7),
    MR_Etc(0x8),
    MR_OnlyInSameField(0x9),
    MR_NearPortal(0xA),
    MR_CreateCountOver(0xB),
    MR_CreateIPCountOver(0xC),
    MR_ExistMiniRoom(0xD),
    MR_NotAvailableField_Game(0xE),
    MR_NotAvailableField_PersonalShop(0xF),
    MR_NotAvailableField_EntrustedShop(0x10),
    MR_OnBlockedList(0x11),
    MR_IsManaging(0x12),
    MG_Tournament(0x13),
    MG_AlreadyPlaying(0x14),
    MG_NotEnoughMoney(0x15),
    MG_InvalidPassword(0x16),
    MR_NotAvailableField_ShopScanner(0x17),
    MR_Expired(0x18),
    MR_TooShortTimeInterval(0x19),
    WR_AlreadyWedding(0x1A),
    WR_CheckPartnersInventory(0x1B),
    MR_NotOnTime(0x1C),
    MR_AlreadyGiftTaken(0x1D),
    MR_NotSameField(0x1E),
    MR_BlockedBehavior(0x1F),
    MR_SignOnlyInSameField(0x20),
    MR_UndefinedOperation(0x21),
    MR_UnableWorld(0x22),
    MR_NotActiveAccount(0x23),;
    @Getter
    private final int value;

    MiniRoomEnterResult(int value) {
        this.value = value;
    }
}
