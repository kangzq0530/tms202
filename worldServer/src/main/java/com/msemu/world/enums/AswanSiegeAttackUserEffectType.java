package com.msemu.world.enums;

/**
 * Created by Weber on 2018/5/3.
 */
public enum AswanSiegeAttackUserEffectType {
    LevelUp(0x0),
    SkillUse(0x1),
    SkillUseBySummoned(0x2),
    SkillAffected(0x3),
    SkillAffectedEx(0x4),
    SkillAffectedSelect(0x5),
    SkillSpecialAffected(0x6),
    Quest(0x7),
    Pet(0x8),
    SkillSpecial(0x9),
    Resist(0xA),
    ProtectOnDieItemUse(0xB),
    PlayPortalSE(0xC),
    JobChanged(0xD),
    QuestComplete(0xE),
    IncDecHPEffect(0xF),
    BuffItemEffect(0x10),
    SquibEffect(0x11),
    MonsterBookCardGet(0x12),
    LotteryUse(0x13),
    ItemLevelUp(0x14),
    ItemMaker(0x15),
    ExpItemConsumed(0x16),
    FieldExpItemConsumed(0x17),
    ReservedEffect(0x18),
    UpgradeTombItemUse(0x19),
    BattlefieldItemUse(0x1A),
    AvatarOriented(0x1B),
    AvatarOrientedRepeat(0x1C),
    AvatarOrientedMultipleRepeat(0x1D),
    IncubatorUse(0x1E),
    PlaySoundWithMuteBGM(0x1F),
    PlayExclSoundWithDownBGM(0x20),
    SoulStoneUse(0x21),
    IncDecHPEffectEX(0x22),
    IncDecHPRegenEffect(0x23),
    EffectUOL(0x24),
    PvPRage(0x25),
    PvPChampion(0x26),
    PvPGradeUp(0x27),
    PvPRevive(0x28),
    JobEffect(0x29),
    FadeInOut(0x2A),
    MobSkillHit(0x2B),
    AswanSiegeAttack(0x2C),
    BlindEffect(0x2D),
    BossShieldCount(0x2E),
    ResetOnStateForOnOffSkill(0x2F),
    JewelCraft(0x30),
    ConsumeEffect(0x31),
    PetBuff(0x32),
    LotteryUIResult(0x33),
    LeftMonsterNumber(0x34),
    ReservedEffectRepeat(0x35),
    RobbinsBomb(0x36),
    SkillMode(0x37),
    ActQuestComplete(0x38),
    Point(0x39),
    SpeechBalloon(0x3A),
    TextEffect(0x3B),
    SkillPreLoopEnd(0x3C),
    Aiming(0x3D),
    PickUpItem(0x3E),
    BattlePvPIncDecHp(0x3F),
    BiteAttackReceiveSuccess(0x40),
    BiteAttackReceiveFail(0x41),
    IncDecHPEffectDelayed(0x42),
    Lightness(0x43),
    UserActionSetUsed(0x44),;
    private byte value;

    AswanSiegeAttackUserEffectType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
