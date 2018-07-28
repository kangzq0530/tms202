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
 * Created by Weber on 2018/4/29.
 */
public enum UserEffectType {

    // 等級提升
    LevelUp(0x0),
    // 近端技能特效
    SkillUse(0x1),
    // 遠端技能特效
    SkillUseBySummoned(0x2),
    // 0x3
    // 特殊技能特效
    SkillAffected(0x4),
    // 機甲戰神-輔助機器特效
    SkillAffectedEx(0x5),
    SkillAffectedSelect(0x6),
    SkillSpecialAffected(0x7),
    // 物品獲得/丟棄文字特效
    Quest(0x8),
    // 寵物等級提升
    Pet(0x9),
    // 技能飛行體特效
    SkillSpecial(0xA),
    // 抵抗異常狀態
    Resist(0xB),
    // 使用護身符
    ProtectOnDieItemUse(0xC),
    UNK_D(0xD),
    PlayPortalSE(0xE),
    // 職業變更
    JobChanged(0xF),
    // 任務完成
    QuestComplete(0x10),
    // 回復特效(Byte)
    IncDecHPEffect(0x11),
    BuffItemEffect(0x12),
    SquibEffect(0x13),
    // 拾取怪物卡片[188-完成]
    MonsterBookCardGet(0x14), // mCardGet
    LotteryUse(0x15),
    ItemLevelUp(0x16),
    ItemMaker(0x17),
    // 0x18 [Int] MESO+
    ExpItemConsumed(0x19),
    // 連續擊殺時獲得的經驗提示
    FieldExpItemConsumed(0x1A),
    // 顯示WZ的效果
    ReservedEffect(0x1B),
    // 聊天窗顯示"消耗1個原地復活術 ，於角色所在原地進行復活！（尚餘Byte個）"
    UpgradeTombItemUse(0x1C),
    BattlefieldItemUse(0x1D),
    // 顯示WZ的效果2
    AvatarOriented(0x1E),
    AvatarOrientedRepeat(0x1F),
    AvatarOrientedMultipleRepeat(0x20),
    IncubatorUse(0x21),
    // WZ聲音
    PlaySoundWithMuteBGM(0x22),
    // WZ聲音
    PlayExclSoundWithDownBGM(0x22),
    UNK_23(0x23),
    // 商城道具效果
    SoulStoneUse(0x24),
    // 回復特效(Int)
    IncDecHPEffectEX(0x25),
    IncDecHPRegenEffect(0x26),
    UNK_27(0x27),
    UNK_28(0x28),
    UNK_29(0x29),
    UNK_2A(0x2A),
    UNK_2B(0x2B),
    UNK_2C(0x2C),
    UNK_2D(0x2D),
    // 採集/挖礦
    EffectUOL(0x2E),
    PvPRage(0x2F),
    PvPChampion(0x30),
    PvPGradeUp(0x31),
    PvPRevive(0x32),
    JobEffect(0x33),
    // 背景變黑
    FadeInOut(0x34),
    MobSkillHit(0x35),
    AswanSiegeAttack(0x36),
    // 影武者出生劇情背景黑暗特效
    BlindEffect(0x37),
    BossShieldCount(0x38),
    // 天使技能充能效果
    ResetOnStateForOnOffSkill(0x39),
    JewelCraft(0x3A),
    ConsumeEffect(0x3B),
    PetBuff(0x3C),
    LotteryUIResult(0x3D),
    LeftMonsterNumber(0x3E),
    ReservedEffectRepeat(0x3F),
    RobbinsBomb(0x40),
    SkillMode(0x41),
    ActQuestComplete(0x42),
    Point(0x43),
    // NPC說話特效
    SpeechBalloon(0x44),
    // 特殊頂部訊息[如燃燒場地]
    TextEffect(0x45),
    // 暗夜行者技能特效
    SkillPreLoopEnd(0x46),
    UNK_47(0x47),
    UNK_48(0x48),
    Aiming(0x49), // Effect/BasicEff.img/aiming/%d
    // 獲得道具頂部提示 UI/UIWindow.img/FloatNotice/%d/DrawOrigin/icon
    PickUpItem(0x4A),
    BattlePvPIncDecHp(0x4B),
    BiteAttackReceiveSuccess(0x4C), // Effect/OnUserEff.img/urus/catch 烏勒斯接住人時候Catch字樣
    BiteAttackReceiveFail(0x4D), // Effect/ItemEff.img/2270002/fail
    IncDecHPEffectDelayed(0x4E),
    Lightness(0x4F),
    // 花狐技能
    ActionSetUsed(0x50),;

    private final int value;

    private UserEffectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

/**
 * enum $4AEE4C1847CC7C99CCABA8FF40789FDD
 * {
 * UserEffect_LevelUp = 0x0,
 * UserEffect_SkillUse = 0x1,
 * UserEffect_SkillUseBySummoned = 0x2,
 * UserEffect_SkillAffected = 0x3,
 * UserEffect_SkillAffected_Ex = 0x4,
 * UserEffect_SkillAffected_Select = 0x5,
 * UserEffect_SkillSpecialAffected = 0x6,
 * UserEffect_Quest = 0x7,
 * UserEffect_Pet = 0x8,
 * UserEffect_SkillSpecial = 0x9,
 * UserEffect_Resist = 0xA,
 * UserEffect_ProtectOnDieItemUse = 0xB,
 * UserEffect_PlayPortalSE = 0xC,
 * UserEffect_JobChanged = 0xD,
 * UserEffect_QuestComplete = 0xE,
 * UserEffect_IncDecHPEffect = 0xF,
 * UserEffect_BuffItemEffect = 0x10,
 * UserEffect_SquibEffect = 0x11,
 * UserEffect_MonsterBookCardGet = 0x12,
 * UserEffect_LotteryUse = 0x13,
 * UserEffect_ItemLevelUp = 0x14,
 * UserEffect_ItemMaker = 0x15,
 * UserEffect_ExpItemConsumed = 0x16,
 * UserEffect_FieldExpItemConsumed = 0x17,
 * UserEffect_ReservedEffect = 0x18,
 * UserEffect_UpgradeTombItemUse = 0x19,
 * UserEffect_BattlefieldItemUse = 0x1A,
 * UserEffect_AvatarOriented = 0x1B,
 * UserEffect_AvatarOrientedRepeat = 0x1C,
 * UserEffect_AvatarOrientedMultipleRepeat = 0x1D,
 * UserEffect_IncubatorUse = 0x1E,
 * UserEffect_PlaySoundWithMuteBGM = 0x1F,
 * UserEffect_PlayExclSoundWithDownBGM = 0x20,
 * UserEffect_SoulStoneUse = 0x21,
 * UserEffect_IncDecHPEffect_EX = 0x22,
 * UserEffect_IncDecHPRegenEffect = 0x23,
 * UserEffect_EffectUOL = 0x24,
 * UserEffect_PvPRage = 0x25,
 * UserEffect_PvPChampion = 0x26,
 * UserEffect_PvPGradeUp = 0x27,
 * UserEffect_PvPRevive = 0x28,
 * UserEffect_JobEffect = 0x29,
 * UserEffect_FadeInOut = 0x2A,
 * UserEffect_MobSkillHit = 0x2B,
 * UserEffect_AswanSiegeAttack = 0x2C,
 * UserEffect_BlindEffect = 0x2D,
 * UserEffect_BossShieldCount = 0x2E,
 * UserEffect_ResetOnStateForOnOffSkill = 0x2F,
 * UserEffect_JewelCraft = 0x30,
 * UserEffect_ConsumeEffect = 0x31,
 * UserEffect_PetBuff = 0x32,
 * UserEffect_LotteryUIResult = 0x33,
 * UserEffect_LeftMonsterNumber = 0x34,
 * UserEffect_ReservedEffectRepeat = 0x35,
 * UserEffect_RobbinsBomb = 0x36,
 * UserEffect_SkillMode = 0x37,
 * UserEffect_ActQuestComplete = 0x38,
 * UserEffect_Point = 0x39,
 * UserEffect_SpeechBalloon = 0x3A,
 * UserEffect_TextEffect = 0x3B,
 * UserEffect_SkillPreLoopEnd = 0x3C,
 * UserEffect_Aiming = 0x3D,
 * UserEffect_PickUpItem = 0x3E,
 * UserEffect_BattlePvP_IncDecHp = 0x3F,
 * UserEffect_BiteAttack_ReceiveSuccess = 0x40,
 * UserEffect_BiteAttack_ReceiveFail = 0x41,
 * UserEffect_IncDecHPEffect_Delayed = 0x42,
 * UserEffect_Lightness = 0x43,
 * User_ActionSetUsed = 0x44,
 * };
 **/