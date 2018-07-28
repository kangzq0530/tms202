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

package com.msemu.commons.enums;

/**
 * Created by Weber on 2018/5/21.
 */
public enum FieldLimit {
    Jump(0),
    UseSkill(1),
    SummonItem(2),
    MysticDoor(3),
    Migrate(4),
    PortalScroll(5),
    UseTeleportItem(6),
    OpenMiniGame(7),
    UseSpecificPortalScroll(8),
    UseTamingMob(9),
    ConsumeStatChangeItem(10),
    ChangePartyBoss(11),
    UseWeddingInvitationItem(13),
    UseCashWeatherItem(14),
    UsePet(15),
    FallDown(17),
    SummonNpc(18),
    ExpLose(20),;
    private int value;

    FieldLimit(int index) {
        this.value = 1 << index;
    }

    public boolean isLimit(int fieldLimit) {
        return (this.value & fieldLimit) > 0;
    }
}
