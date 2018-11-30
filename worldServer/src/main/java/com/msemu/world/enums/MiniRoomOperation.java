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

public enum MiniRoomOperation {

    TRP_PutItem1(0),
    TRP_PutItem2(1),
    TRP_PutItem3(2),
    TRP_PutItem4(3),
    TRP_PutMoney1(4),
    TRP_PutMoney2(5),
    TRP_PutMoney3(6),
    TRP_PutMoney4(7),
    TRP_Trade1(8),
    TRP_Trade2(9),
    TRP_Trade3(0xA),
    TRP_Trade4(0xB),
    TRP_UnTrade(0xC),
    TRP_MoveItemToInventory(0xD),
    TRP_ItemCRC(0xE),
    TRP_TradeRestrainItem(0xF),
    MRP_Create(0x10),
    MRP_CreateCancel(0x11),
    MRP_CreateResult(0x12),
    MRP_Enter(0x13),
    MRP_EnterResult(0x14),
    MRP_Invite(0x15),
    MRP_InviteResult(0x16),
    MRP_GameMessage(0x17),
    MRP_Chat(0x18),
    MRP_UserChat(0x19),
    MRP_Balloon(0x1A),
    MRP_Avatar(0x1B),
    MRP_Leave(0x1C),
    MRP_NotAvailableField(0x1D),
    MRP_CheckSSN2(0x1E),
    ESP_PutItem1(0x1F),
    ESP_PutItem2(0x20),
    ESP_PutItem3(0x21),
    ESP_PutItem4(0x22),
    ESP_BuyItem1(0x23),
    ESP_BuyItem2(0x24),
    ESP_BuyItem3(0x25),
    ESP_BuyItem4(0x26),
    ESP_PutPurchaseItem(0x27),
    ESP_SellItem(0x28),

    // TODO




    None(-1),

    ;

    @Getter
    private final int value;

    MiniRoomOperation(int value) {
        this.value = value;
    }


    public static MiniRoomOperation getByValue(int value) {
        for(MiniRoomOperation type : values()) {
            if (type.getValue() == value)
                return type;
        }
        return None;
    }


}
