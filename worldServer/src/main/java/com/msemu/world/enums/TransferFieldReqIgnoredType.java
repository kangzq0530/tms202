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
public enum TransferFieldReqIgnoredType {
    ENTRANCE_CLOSED(1), // 現在關閉入口
    CANT_MOVE_TO_THAT_AREA(2), //  無法移動到該區。
    CANT_ENTER_BECAUSE_OF_THE_POWER_OF_GROUND(3), // 因受到大地力量的阻擾，無法繼續前進。
    TELEPORT_FORBIDDEN(4),
    CANT_ENTER_BECAUSE_OF_THE_POWER_OF_GROUND_2(5), // 因受到大地力量的阻擾，無法繼續前進。
    MAP_ONLY_PARTY_MEMBER(6), // 只有隊員能入場的地圖。
    ONLY_PARTY_BOSS_CAN_APPLY(7), // 只有隊長可以申請入場。
    ONLY_ENTER_WHEN_RECRUIT_COMPLETE(8), // 成員全部募集完畢才可以進場。
    ONLY_FOR_EXPEDITION_MEMBER(9), // 只有遠征隊員才可進入的地圖。
    BEHAVIOR_FORBIDDEN(16), // 您因不當行為，而遭遊戲管理員禁止攻擊、禁止交易、禁止丟棄道具、禁止開啟個人商店與精靈商人、禁止組隊、禁止使用拍賣系統、無法使用各種傳送工具進行移動，因此無法使用該功能。
    MAP_LOADING_FULL(17), // 要進入的地圖人數已滿。請使用其他分流進入，或請稍候。

    ;
    @Getter
    private int value;

    TransferFieldReqIgnoredType(int value) {
        this.value = value;
    }


}
