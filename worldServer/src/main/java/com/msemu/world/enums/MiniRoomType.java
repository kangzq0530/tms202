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

public enum MiniRoomType {
    MR_NOT_DEFINED(0x0),
    MR_OmokRoom(0x1),
    MR_MemoryGameRoom(0x2),
    MR_RpsGameRoom(0x3),
    MR_TradingRoom(0x4),
    MR_PersonalShop(0x5),
    MR_EntrustedShop(0x6),
    MR_CashTradingRoom(0x7),
    MR_WeddingExRoom(0x8),
    MR_CandyTradingRoom(0x9),
    MR_MultiYutRoom(0xA),
    MR_SignRoom(0xB),
    MR_TenthAnniversaryBoardGameRoom(0xC),
    MR_BingoGameRoom(0xD),
    MR_OmokRenewalRoom(0xE),
    MR_MemoryGameRoom_2013(0xF),
    MR_OneCardGameRoom(0x10),
    MR_SuperMultiYutRoom(0x11),
    MR_RunnerGameRoom(0x12),
    MR_TypeNo(0x13),
    None(-1),;
    @Getter
    private final int value;

    MiniRoomType(int value) {
        this.value = value;
    }

    public static MiniRoomType getByValue(int value) {
        for (MiniRoomType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return None;
    }

}
