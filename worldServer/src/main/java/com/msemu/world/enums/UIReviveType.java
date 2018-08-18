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

public enum UIReviveType {
    None(-1),
    // 正常死亡
    Normal(0),
    // 使用組隊點數
    UsingPartyPoint(1),
    // 空白
    SoulDungeon(2),
    // 所在地圖復活
    MagnusNormalHard(3),
    OnUIDeathCountInfo(4),
    // 靈魂之石力量復活
    SoulStone(5),
    // 原地復活
    UpgradeTombItem(6),
    // 高級票券使用者
    PremiumUser(7),
    // 伊露納的幸運機會，可以馬上復活
    UpgradeTombItemOfCashBuffEvent(8),
    // 使用楓葉點數進行復活
    UpgradeTombItemMaplePoint(9),
    // 跟 3 一樣
    UIReviveType_10(10),
    // 戰鬥機器人
    BattleRoid(11),
    // 戰鬥機器人
    DreamWorld(12),
    // UI/UIWindows2.img/Notice/2 沒有這個節點
    UIReviveType_13(13),
    // 跟原地復活術依樣
    UIReviveType_14(14),
    NoUI(15),
    ;

    @Getter
    private final int value;

    UIReviveType(int value) {
        this.value = value;
    }


}
