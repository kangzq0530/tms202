package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/12.
 */
public enum MobStat {

    //  =========== MASK[0]
    //物攻
    PAD(0),
    //物防
    PDR(1),
    //魔攻
    MAD(2),
    //魔防
    MDR(3),
    //命中
    ACC(4),
    //迴避
    EVA(5),
    //速度[完成]
    Speed(6),
    //暈眩
    Stun(7),
    //結冰、麻痺
    Freeze(8),
    //中毒[完成]
    Poison(9),
    //封印、沉默
    Seal(10),
    //黑暗
    Darkness(11),
    //物攻提升
    PowerUp(12),
    //魔攻提升
    MagicUp(13),
    //物防提升
    PGuardUp(14),
    //魔防提升
    MGuardUp(15),
    //攻擊免疫
    PImmune(16),
    //魔法免疫
    MImmune(17),
    //影網
    Web(18),
    //
    HardSkin(19),
    //忍者伏擊
    Ambush(20),
    //武器涂毒
    Venom(21),
    //致盲
    Blind(22),
    //技能封印
    SealSkill(23),
    Dazzle(24),
    //反射物攻
    PCounter(25),
    //反射魔攻
    MCounter(26),
    //痛苦提升
    RiseByToss(27),
    BodyPressure(28),
    Weakness(29),
    // 無法觸碰角色
    Showdown(30),
    //魔法無效
    MagicCrash(31),
    // =========== MASK[1]

    DamagedElemAttr(32),
    Dark(33),
    Mystery(34),
    AddDamParty(35),
    HitCriDamR(36),
    Fatality(37),
    Lifting(38),
    //死亡
    DeadlyCharge(39),
    Smite(40),
    AddDamSkill(41),
    Incizing(42),
    DodgeBodyAttack(43),
    DebuffHealing(44),
    AddDamSkill2(45),
    BodyAttack(46),
    TempMoveAbility(47),
    FixDamRBuff(48),
    ElementDarkness(49),
    // 另一個咬擊
    AreaInstallByHit(50),
    BMageDebuff(50),
    JaguarProvoke(51),
    JaguarBleeding(52),
    DarkLightning(53),
    PinkbeanFlowerPot(54),
    BattlePvPHelenaMark(55),
    PsychicLock(56),
    PsychicLockCoolTime(57),
    PsychicGroundMark(58),
    PowerImmune(59),
    PsychicForce(60),
    MultiPMDR(61),
    MBS62(62),
    ElementResetBySummon(63),
    BahamutLightElemAddDam(64),
    BossPropPlus(65),
    MultiDamSkill(66),
    RWLiftPress(67),
    RWChoppingHammer(68),
    TimeBomb(69),
    Treasure(70),
    AddEffect(71),
    MBS72(72),
    Invincible(73, true),
    Explosion(74),
    HangOver(75),
    //持續扣血 - 破滅之輪
    BurnedInfo(76, true),
    InvincibleBalog(77, true),
    // =========== MASK[2]
    ExchangeAttack(78, true),
    ExtraBuffStat(79, true),
    LinkTeam(80, true),
    SoulExplosion(81, true),
    SeperateSoulP(82, true),
    SeperateSoulC(83, true),
    Ember(84, true),
    TrueSight(85, true),
    MBS86(86, true),
    Laser(87, true),
    DEFAULT_12(88, true),
    DEFAULT_13(89, true),
    MBS90(90),
    MBS91(91),
    MBS92(92),
    MBS93(93),
    MBS94(94),
    MBS95(95),;
    static final long serialVersionUID = 0L;
    private final int i;
    private final int position;
    private final boolean isDefault;
    private final int bitNumber;

    public static final int MAX_LENGTH = 3;

    private MobStat(int i) {
        this.i = 1 << (31 - (i % 32)); // 如果要變舊的，就把減31去掉，詳細請參考頂端說明
        this.position = (int) Math.floor(i / 32);
        this.isDefault = false;
        this.bitNumber = i;
    }

    private MobStat(int i, boolean end) {
        this.i = 1 << (31 - (i % 32)); // 如果要變舊的，就把減31去掉，詳細請參考頂端說明
        this.position = (int) Math.floor(i / 32);
        this.isDefault = end;
        this.bitNumber = i;
    }

    public int getPosition() {
        return position;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public int getBitNumber() {
        return bitNumber;
    }

    public int getValue() {
        return i;
    }

    public boolean isMovementAffectingStat() {
        switch (this) {
            case Speed:
            case Stun:
            case Freeze:
            case RiseByToss:
            case Lifting:
            case Smite:
            case TempMoveAbility:
            case RWLiftPress:
                return true;
            default:
                return false;
        }
    }


    @Override
    public String toString() {
        return this.name() + "(" + this.bitNumber + ", " + (this.isDefault ? "true" : ":") + ")";
    }
}
