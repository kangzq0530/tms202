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

/**
 * Created by Weber on 2018/4/13.
 */
public enum WvsMessageType {

    DropPickUpMessage(0x0),
    QuestRecordMessage(0x1),
    QuestRecordMessageAddValidCheck(0x2),
    CashItemExpireMessage(0x3),
    IncEXPMessage(0x4),
    IncSPMessage(0x5),
    IncPOPMessage(0x6),
    IncMoneyMessage(0x7),
    IncGPMessage(0x8),
    IncCommitmentMessage(0x9),
    GiveBuffMessage(0xA),
    GeneralItemExpireMessage(0xB),
    SystemMessage(0xC),
    // 0xD
    QuestRecordExMessage(0xE),
    WorldShareRecordMessage(0xF),
    ItemProtectExpireMessage(0x10),
    ItemExpireReplaceMessage(0x11),
    ItemAbilityTimeLimitedExpireMessage(0x12),
    SkillExpireMessage(0x13),
    IncNonCombatStatEXPMessage(0x14),
    LimitNonCombatStatEXPMessage(0x15),
    RecipeExpireMessage(0x16),
    AndroidMachineHeartAlertMessage(0x17),
    IncFatigueByRestMessage(0x18),
    IncPvPPointMessage(0x19),
    PvPItemUseMessage(0x1A),
    WeddingPortalError(0x1B),
    PvPHardCoreExpMessage(0x1C),
    NoticeAutoLineChanged(0x1D),
    EntryRecordMessage(0x1E),
    EvolvingSystemMessage(0x1F),
    EvolvingSystemMessageWithName(0x20),
    CoreInvenOperationMessage(0x21),
    NxRecordMessage(0x22),
    BlockedBehaviorTypeMessage(0x23),
    IncWPMessage(0x24),
    MaxWPMessage(0x25),
    StylishKillMessage(0x27),
    //BarrierEffectIgnoreMessage
    ExpiredCashItemResultMessage(0x27),
    CollectionRecordMessage(0x28),
    RandomChanceMessage(0x29),
    UNK_2A(0x2A),
    UNK_2B(0x2B),
    UNK_2C(0x2C),
    UNK_2D(0x2D),
    UNK_2E(0x2E),
    UNK_2F(0x2F),;

    private final int value;

    private WvsMessageType(int value) {
        this.value = value;
    }

    public static WvsMessageType getType(int type) {
        for (WvsMessageType msT : values()) {
            if (msT.getValue() == type) {
                return msT;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
