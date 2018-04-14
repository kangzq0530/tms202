package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum DBChar {
    All(0xFFFFFFFFFFFFFFFFL),
    Character(0x1),
    Money(0x2),
    ItemSlotEquip(0x4),
    ItemSlotConsume(0x8),
    ItemSlotInstall(0x10),
    ItemSlotEtc(0x20),
    ItemSlotCash(0x40),
    InventorySize(0x80),
    SkillRecord(0x100),
    QuestRecord(0x200),
    MiniGameRecord(0x400),
    CoupleRecord(0x800),
    MapTransfer(0x1000),
    QuestComplete(0x4000),
    Flag80000(0x80000),
    MonsterBookCard(0x10000),
    MonsterBookCover(0x20000),
    SkillCoolTime(0x8000),
    QuestRecordEx(0x40000),
    ItemPot(0x800000),
    CoreAura(0x1000000),
    ExpConsumeItem(0x2000000),
    Unsure(0x40000000),
    AdminShopCount(0x100000),
    WildHunterInfo(0x200000),
    ShopBuyLimit(0x4000000),
    ZeroInfo(0x80000000000L),
    StolenSkills(0x20000000),
    ChosenSkills(0x10000000),
    CharacterPotentialSkill(0x80000000L),
    SoulCollection(0x40000000000000L),
    HonorInfo(0x100000000L),
    Avatar(0x2000000000L),
    Flag8000000000L(0x8000000000L),
    Flag100000000000(0x100000000000L),
    Flag200000000000(0x200000000000L),
    OXSystem(0x400000000000L),
    ExpChairInfo(0x800000000000L),
    RedLeafInfo(0x1000000000000L),
    Flag2000000000000(0x2000000000000L),
    VMatrixInfo(0x2000000000000000L),
    CoreInfo(0x1000000000L),
    EntryRecord(0x200000000L),
    ReturnEffectInfo(0x400000000L),
    DressUpInfo(0x800000000L),
    Flag20000000000000L(0x20000000000000L),
    LikePoint(0x40000000000L),
    RunnerGameRecord(0x80000000000L),

    ;

    public long uFlag;

    DBChar(long uFlag) {
        this.uFlag = uFlag;
    }

    public long get() {
        return uFlag;
    }

    public boolean isInMask(long mask) {
        return (mask & get()) != 0;
    }

    public boolean isInMask(DBChar mask) {
        return (mask.get() & get()) != 0;
    }
}
