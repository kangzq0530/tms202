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
 * Created by Weber on 2018/5/12.
 */
public enum TransferChannelReqIgnoredType {

    CANT_TRANSFER_TO_CHANNEL_NOW(0), // 目前無法移動到該頻道！請稍後重新再試。
    CANT_TRANSFER_TO_CASHSHOP_NOW(1), //  目前無法進入購物商場！請稍後重新再試。
    CANT_TRANSFER_TO_BIDDING_PLACE_NOT(2), // 現在無法移動到競標場。等一下再試。
    ONLY_CAN_USE_IN_PVE_CHANNEL(4),  // 只有在PVE伺服器裡可以使用。
    BEHAVIOR_LIMIT(5),   // 皮卡啾無法執行的事情。 || 根據帳號保護政策，限制此帳號商城使用功能。
    CANT_ENTER_NOT(6),   // 目前無法進入，請玩家稍後再試。
    AUCTION_SYSTEM_HIGH_LOADING(7), // 目前拍賣系統擁塞中，請稍後再試！
    BEHAVIOR_FORBIDDEN(8), // 您因不當行為，而遭遊戲管理員禁止攻擊、禁止交易、禁止丟棄道具、禁止開啟個人商店與精靈商人、禁止組隊、禁止使用拍賣系統、無法使用各種傳送工具進行移動，因此無法使用該功能。
    CANT_OPERATE_NOT(9), // 現在無法操作。
    CANT_TRANSFER_TO_CHANNEL_NOW_2(11), // 目前無法移動到該頻道！請稍後重新再試。
    EVENT_CANT_EXECUTE_NOW(12), // 目前遊戲中無法進行的事情。
    CANT_USE_BIDDING_PLACE_HERE(13), // 這裡無法使用競標場
    TOO_MANY_USER_MIGHT_DELAY(14), // 服務器用戶太多，連接出現延遲。請重新嘗試。
    ;
    @Getter
    private int value;

    TransferChannelReqIgnoredType(int value) {
        this.value = value;
    }
}
