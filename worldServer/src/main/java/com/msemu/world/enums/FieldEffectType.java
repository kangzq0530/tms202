package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum FieldEffectType {
    FieldEffectSummon(0),
    Tremble(1),
    Object(2),
    ObjectDisable(3),
    FieldEffectScreen(4),
    PlaySound(5),
    MobHPTag(6),
    ChangeBGM(7),
    BGMVolumeOnly(8),
    SetBGMVolume(9),
    RewardRoullet(10),
    Unk1(11),
    Unk2(12),
    ScreenDelayed(13),
    ScreenEffect(14),
    ScreenFloatingEffect(15),
    CreateWindowMaybeidk(16),
    SetGrey(17),
    OnOffLayer(18),
    SomeAnimation(19),
    MoreAnimation(20),
    RemoveAnimation(21),
    ChangeColor(22),
    StageClearExpOnly(23),
    EvenMoreAnimation(24),
    SkeletonAnimation(25),
    OneTimeSkeletonAnimation(26),;

    private byte value;

    FieldEffectType(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }
}