package com.msemu.world.client.character.stats;

/**
 * Created by Weber on 2018/4/11.
 */
public enum CharacterTemporaryStat {
    NONE_START(-1),
    //==========================Mask[0] - 1 - IDA[0xE]

    //物理攻擊力提升                    []
    IndiePAD(0, true),
    //魔法攻擊力提升                    []
    IndieMAD(1, true),
    //物理防御力提升                    []
    IndiePDD(2, true),
    //魔法防御力提升                    []
    //    IndieMDD(-1, true),
    //最大體力提升                      []
    IndieMHP(3, true), //indieMaxHp, indieMhp
    //最大體力百分比提升                 []
    IndieMHPR(4, true), //indieMhpR
    //最大魔法提升                      []
    IndieMMP(5, true),//indieMaxMp
    //最大魔法百分比提升                 []
    IndieMMPR(6, true), //indieMmpR
    //命中值提升                        []
    IndieACC(7, true),//indieAcc
    //迴避值提升                        []
    IndieEVA(8, true),
    //跳躍力提升                        []
    IndieJump(9, true),
    //移動速度提升                       [確定完成]
    IndieSpeed(10, true),
    //全屬性提升                        [確定完成]
    IndieAllStat(11, true), //indieAllStat
    //
    IndieDodgeCriticalTime(12, true),
    //經驗值獲得量提升                  []
    IndieEXP(14, true),
    //攻擊速度提升                      [確定完成]
    IndieBooster(15, true), //indieBooster
    //
    IndieFixedDamageR(15, true),
    //
    PyramidStunBuff(16, true),
    //
    PyramidFrozenBuff(17, true),
    //
    PyramidFireBuff(18, true),
    //
    PyramidBonusDamageBuff(19, true),
    //
    IndieRelaxEXP(20, true),
    //力量提升                         []
    IndieSTR(21, true),
    //敏捷提升                         []
    IndieDEX(22, true),
    //智力提升                         []
    IndieINT(23, true),
    //幸運提升                         []
    IndieLUK(24, true),
    //傷害百分比提升                    []
    IndieDamR(25, true),//indieDamR
    //
    IndieScriptBuff(26, true),
    //
    IndieMDF(27, true),
    //==========================Mask[1] - 2 - IDA[0xD]

    //傷害最大值提升                    []
    IndieMaxDamageOver(28, true),
    //異常抗性提升                      []
    IndieAsrR(29, true), //indieAsrR
    //屬性抗性提升                      []
    IndieTerR(30, true), //indieTerR
    //爆擊率提升                        []
    IndieCr(31, true),
    //物理防禦率提升                    []
    IndiePDDR(32, true),
    //最大爆擊提升                      []
    IndieCrMax(33, true),
    //BOSS攻擊力提升                    []
    IndieBDR(34, true),
    //全屬性百分比提升                  []
    IndieStatR(35, true),
    //格擋提升                          []
    IndieStance(36, true),
    //無視怪物防禦率提升                 []
    IndieIgnoreMobpdpR(37, true), //indieIgnoreMobpdpR
    //
    IndieEmpty(38, true),
    //物理攻擊力百分比提升               []
    IndiePADR(39, true),
    //魔法攻擊力百分比提升               []
    IndieMADR(40, true),
    //最大爆擊傷害提升                   []
    IndieCrMaxR(41, true),
    //迴避值百分比提升                   []
    IndieEVAR(42, true),
    //魔法防禦率提升                     []
    IndieMDDR(43, true),
    //
    IndieDrainHP(44, true),
    //
    IndiePMdR(45, true),
    //傷害最大值百分比提升
    IndieMaxDamageOverR(46, true),
    //
    IndieForceJump(47, true),
    //
    IndieForceSpeed(48, true),
    //
    IndieQrPointTerm(49, true),
    //
    IDA_BUFF_50(50, true),
    //
    IDA_BUFF_51(51, true),
    //
    IDA_BUFF_52(52, true),
    //
    IDA_BUFF_53(53, true),
    //
    IndieStatCount(54, true),
    //
    IDA_BUFF_55(55, true),
    //物理攻擊力                        [完成]
    PAD(57),
    //物理防御力                        [完成]
    PDD(58),
    //魔法攻擊力                        [完成]
    MAD(59),
    //魔法防御力                        [完成]
    //    MDD(-1),
    //命中率                            [完成]
    ACC(60),
    //迴避率                            [完成]
    EVA(61),
    //手技                              [完成]
    Craft(62),
    //移動速度                          [確定完成]
    Speed(63),
    //跳躍力                            [確定完成]
    Jump(64),
    //魔心防禦                          [完成]
    MagicGuard(65),
    //隱藏術                            [完成]
    DarkSight(66),
    //攻擊加速                          [確定完成]
    Booster(67),
    //傷害反擊                          [完成]
    PowerGuard(68),
    //神聖之火_最大MP                   [完成]
    MaxHP(69),
    //==========================Mask[2] - 3 - IDA[0xC]

    //神聖之火_最大HP                   [完成]
    MaxMP(70),
    //神聖之光                          [完成]
    Invincible(71),
    //無形之箭                          [完成]
    SoulArrow(72),
    //昏迷                              [完成]
    Stun(73),
    //中毒                              [完成]
    Poison(74),
    //封印                              [完成]
    Seal(75),
    //黑暗                              [完成]
    Darkness(76),
    //鬥氣集中                          [完成]
    ComboCounter(77),
    // 更新BUFF用
    IDA_BUFF_78(78),
    IDA_BUFF_79(79),
    IDA_BUFF_80(80),
    //屬性攻擊                          [確定完成]
    WeaponCharge(81),
    //神聖祈禱                          [完成]
    HolySymbol(82),
    //楓幣獲得量                        [完成]
    MesoUp(83),
    //影分身                            [確定完成]
    ShadowPartner(84),
    //勇者掠奪術                        [完成]
    PickPocket(85),
    //楓幣護盾                          [完成]
    MesoGuard(86),
    //                                 [完成]
    Thaw(87),
    //虛弱                              [完成]
    Weakness(88),
    //詛咒                              [完成]
    Curse(89),
    //緩慢                              [完成]
    Slow(90),
    //變身                              [完成]
    Morph(91),
    //恢復                              [完成]
    Regen(92),
    //楓葉祝福                          [確定完成]
    BasicStatUp(93),
    //格擋                              []
    Stance(94),
    //銳利之眼                          [完成]
    SharpEyes(95),
    //魔法反擊                          [完成]
    ManaReflection(96),
    //誘惑                              [完成]
    Attract(97),
    //不消耗發射道具                     [完成]
    NoBulletConsume(98),
    //魔力無限                          [完成]
    Infinity(99),
    //進階祝福                                [完成]
    AdvancedBless(100),
    //                                 []
    IllusionStep(101),
    //致盲                              []
    Blind(102),
    //集中精力                          []
    Concentration(103),
    //不死化                            []
    BanMap(104),
    //英雄的回響                        []
    MaxLevelBuff(105),
    //==========================Mask[3] - 4 - IDA[0xB]

    //楓幣獲得量(道具)                   [不確定]
    MesoUpByItem(106),
    IDA_BUFF_107(107), // 106 107 108 112
    IDA_BUFF_108(108), //  106 107 108 112
    //鬼魂變身                          [完成]
    Ghost(109),
    //                                  [完成]
    Barrier(110),
    //混亂                              [完成]
    ReverseInput(111),
    //掉寶幾率(道具)                     [完成]
    ItemUpByItem(112),
    //物理免疫                           [完成]
    RespectPImmune(113),
    // 魔法免疫                          [完成]
    RespectMImmune(114),
    //解多人Buff用的                    [完成]
    DefenseAtt(115),
    //解多人Buff用的                    [完成]
    DefenseState(116),
    //最終傷害(道場技能:地火天爆/道具:法老的祝福)[]
    DojangBerserk(117),
    //金剛不壞_無敵                      [完成]
    DojangInvincible(118),
    //金剛不壞_盾牌效果                  [完成]
    DojangShield(119),
    //聖魂劍士終極攻擊                   [完成]
    SoulMasterFinal(120),
    //破風使者終極攻擊                   []
    WindBreakerFinal(121),
    //自然力重置                         [確定完成]
    ElementalReset(122),
    //風影漫步                          []
    HideAttack(123),
    //組合無限                          []
    EventRate(124),
    //矛之鬥氣                          []
    ComboAbilityBuff(125),
    //嗜血連擊                          [202]
    ComboDrain(126),
    //宙斯之盾                          []
    ComboBarrier(127),
    //強化連擊(CMS_戰神抗壓)            [確定完成]
    BodyPressure(128),
    //釘錘(CMS_戰神威勢)                []
    RepeatEffect(129),
    //(CMS_天使狀態)                    []
    ExpBuffRate(129),
    //無法使用藥水                      []
    StopPortion(130),
    //影子                              []
    StopMotion(131),
    //恐懼                              []
    Fear(132),
    //
    IDA_BUFF_133(133),
    //緩慢術                            [完成]
    HiddenPieceOn(134),
    //守護之力(CMS_魔法屏障)             []
    MagicShield(135),
    //魔法抵抗．改                      []
    MagicResistance(136),
    //靈魂之石                          []
    SoulStone(137),
    //==========================Mask[4] - 5 - IDA[0xA]

    //飛行                              []
    Flying(138),
    //冰凍                              [完成]
    Frozen(139),
    //雷鳴之劍                      [完成]
    AssistCharge(140),
    //鬥氣爆發                          [完成]
    Enrage(141),
    //障礙                              [完成]
    DrawBack(142),
    //無敵(隱‧鎖鏈地獄、自由精神等)    [確定完成]
    NotDamaged(143),
    //絕殺刃                            [完成]
    FinalCut(144),
    //咆哮                              [完成]
    HowlingAttackDamage(145),
    //狂獸附體                          [完成]
    BeastFormDamageUp(146),
    //地雷                              [完成]
    Dance(147),
    //增強_MaxHP                        [完成]
    EMHP(148),
    //增強_MaxMP                        [完成]
    EMMP(149),
    //增強_物理攻擊力                   [完成]
    EPAD(150),
    //增強_魔法攻擊力                   [確定完成]
    EMAD(151),
    //增強_物理防禦力                   [確定完成]
    EPDD(152),
    //增強_魔法防禦力                   []
    //EMDD(-1),
    //全備型盔甲                        [完成]
    Guard(153),
    //
    IDA_BUFF_154(154),
    //
    IDA_BUFF_155(155),
    //終極賽特拉特_PROC                 [完成]
    Cyclone(156),
    IDA_BUFF_157(157),
    // 咆哮_會心一擊機率增加            [完成]
    HowlingCritical(158),
    // 咆哮_MaxMP 增加                   [完成]
    HowlingMaxMP(159),
    // 咆哮_傷害減少                     [完成]
    HowlingDefence(160),
    // 咆哮_迴避機率                     []
    HowlingEvasion(161),
    //                                   [完成]
    Conversion(162),
    //甦醒                              [完成]
    Revive(163),
    //迷你啾出動                        [完成]
    PinkbeanMinibeenMove(164),
    //潛入                             [完成]
    Sneak(165),
    //合金盔甲                         [完成]
    Mechanic(166),
    //==========================Mask[5] - 6 - IDA[0x9]

    //暴走形态                          [完成]
    BeastFormMaxHP(167),
    //幸运骰子                          [完成]
    Dice(168),
    //祝福护甲                          [完成]
    BlessingArmor(169),
    //攻擊增加百分比                    [完成]
    DamR(170),
    //瞬間移動精通                      [完成]
    TeleportMasteryOn(171),
    //戰鬥命令                          [完成]
    CombatOrders(172),
    //追隨者                            [完成]
    Beholder(173),
    //裝備潛能無效化                    [完成]
    DispelItemOption(174),
    //                                  [完成]
    Inflation(175),
    //龍神的庇護                        [完成]
    OnixDivineProtection(176),
    //未知                              [完成]
    Web(177),
    //天使祝福                          [完成]
    Bless(178),
    //解多人Buff用的                    [完成]
    TimeBomb(179),
    //解多人Buff用的                    [完成]
    Disorder(180),
    //解多人Buff用的                    [完成]
    Thread(181),
    //                                 [完成]
    Team(182),
    //解多人Buff用的                    [完成]
    Explosion(183),
    //                                 [完成]
    BuffLimit(184),
    //力量                              [完成]
    STR(185),
    //智力                              [完成]
    INT(186),
    //敏捷                              [完成]
    DEX(187),
    //幸運                              [完成]
    LUK(188),
    //未知(未確定)                      [完成]
    DispelItemOptionByField(189),
    //龍捲風(異常狀態)                  [完成]
    DarkTornado(190),
    //未知(未確定)                      [完成]
    PVPDamage(191),
    //未知                              [完成]
    PvPScoreBonus(192),
    // 更新BUFF用                       [完成]
    PvPInvincible(193),
    //解多人Buff用的                    [完成]
    PvPRaceEffect(194),
    //解多人Buff用的                    [完成]
    WeaknessMdamage(195),
    //凍結                              [完成]
    Frozen2(196),
    //解多人Buff用的                    [完成]
    PVPDamageSkill(197),
    //未知(未確定)(90002000)            [完成]
    AmplifyDamage(198),
    //冰騎士                            []
    //    IceKnight(-1),
    //更新BUFF用                        [完成]
    Shock(199),
    //無限力量                          [完成]
    InfinityForce(200),
    // 更新BUFF用                       [完成]
    IncMaxHP(201),
    //==========================Mask[6] - 7 - IDA[0x8]

    //未知(未確定)                      [完成]
    IncMaxMP(202),
    //聖十字魔法盾                      [完成]
    HolyMagicShell(203),
    //無需蓄力[核爆術]                  [完成]
    KeyDownTimeIgnore(204),
    //神秘狙擊                         [完成]
    ArcaneAim(205),
    //大魔法師                         [完成]
    MasterMagicOn(206),
    //異常抗性                         [完成]
    AsrR(207),
    //屬性抗性                         [完成]
    TerR(208),
    //水之盾                           [完成]
    DamAbsorbShield(209),
    //變形                             [完成]
    DevilishPower(210),
    //随机橡木桶                       [完成]
    Roulette(211),
    //靈魂灌注_攻擊增加                [完成]
    SpiritLink(212),
    //神圣拯救者的祝福                 [完成]
    AsrRByItem(213),
    //光明綠化                        [完成]
    Event(214),
    //靈魂灌注_爆擊率增加              [完成]
    CriticalBuff(215),
    // 更新BUFF用                     [完成]
    DropRate(216),
    // 更新BUFF用                     [完成]
    PlusExpRate(217),
    // 更新BUFF用                     [完成]
    ItemInvincible(218),
    // 更新BUFF用
    Awake(219),
    // 更新BUFF用                     [完成]
    ItemCritical(220),
    // 更新BUFF用                     [完成]
    ItemEvade(221),
    //未知(未確定)                    [完成]
    Event2(222),
    //吸血鬼之觸                      [完成]
    VampiricTouch(223),
    //提高防禦力[猜測]                [完成]
    DDR(224),
    // 更新BUFF用                     []
    IncCriticalDamMin(-1),
    // 更新BUFF用                     []
    IncCriticalDamMax(-1),
    // 更新BUFF用                     [完成]
    IncTerR(225),
    // 更新BUFF用                     [完成]
    IncAsrR(226),
    // 更新BUFF用                     [完成]
    DeathMark(227),
    // 更新BUFF用                     [完成]
    UsefulAdvancedBless(228),
    // 更新BUFF用                     [完成]
    Lapidification(229),
    // 更新BUFF用                     [完成]
    VenomSnake(230),
    // 更新BUFF用                     [完成]
    CarnivalAttack(231),
    //==========================Mask[7] - 8 - IDA[0x7]

    //Carnival防禦                     [完成]
    CarnivalDefence(232),
    // 更新BUFF用                     [完成]
    CarnivalExp(233),
    // 更新BUFF用                     [完成]
    SlowAttack(234),
    //角設預設Buff                    [完成]
    PyramidEffect(235),
    //角設預設Buff                    [完成]
    KillingPoint(236),
    // 更新BUFF用                     [完成]
    HollowPointBullet(237),
    //按壓型技能進行                   [完成]
    KeyDownMoving(238),
    //無視防禦力                       [完成]
    IgnoreTargetDEF(239),
    //復活一次                         [完成]
    ReviveOnce(240),
    //幻影斗蓬                         [完成]
    Invisible(241),
    //爆擊機率增加                     [完成]
    EnrageCr(242),
    //最小爆擊傷害                     [完成]
    EnrageCrDamMin(243),
    //審判                             [完成]
    Judgement(244),
    //增加_物理攻擊                     [完成]
    DojangLuckyBonus(245),
    // 更新BUFF用                   [完成]
    PainMark(246),
    //IDA移動Buff                  [完成]
    Magnet(247),
    //IDA移動Buff                  [完成]
    MagnetArea(248),
    // 更新BUFF用 [完成]
    IDA_BUFF_249(249),
    // 更新BUFF用 [完成]
    IDA_BUFF_250(250),
    //IDA移動Buff                  [完成]
    VampDeath(251),
    //解多人Buff用的                [完成]
    BlessingArmorIncPAD(252),
    //黑暗之眼                     [完成]
    KeyDownAreaMoving(253),
    //光蝕 & 暗蝕                  [完成]
    Larkness(254),
    //黑暗強化 & 全面防禦          [完成]
    StackBuff(255),
    //黑暗祝福                     [完成]
    BlessOfDarkness(256),
    //抵禦致命異常狀態              [完成]
    //(如 元素適應(火、毒), 元素適應(雷、冰), 聖靈守護)
    AntiMagicShell(257),
    //血之限界                     [完成]
    LifeTidal(258),
    // 更新BUFF用                   [完成]
    HitCriDamR(259),
    //凱撒變型值                   [完成]
    SmashStack(260),
    //==========================Mask[8] - 9 - IDA[0x6]

    //堅韌護甲                     [完成]
    PartyBarrier(261),
    //凱撒模式切換                 [完成]
    ReshuffleSwitch(262),
    // 更新BUFF用                   [完成]
    SpecialAction(263),
    //IDA移動Buff                  [完成]
    VampDeathSummon(264),
    //意志之劍                     [完成]
    StopForceAtomInfo(265),
    //會心一擊傷害                 [完成]
    SoulGazeCriDamR(266),
    // 更新BUFF用                   [完成]
    SoulRageCount(267),
    // 靈魂傳動                      [完成]
    PowerTransferGauge(268),
    // 天使親和力                   [完成]
    AffinitySlug(269),
    // 三位一體                    [完成]
    Trinity(270),
    //IDA特殊Buff                   [完成]
    IncMaxDamage(271),
    // 更新BUFF用                  [完成]
    BossShield(272),
    //功能不知道                     [完成]
    MobZoneState(273),
    //IDA移動Buff                   [完成]
    GiveMeHeal(274),
    // 更新BUFF用                   [完成]
    TouchMe(275),
    // 更新BUFF用                   [完成]
    Contagion(276),
    // 更新BUFF用
    ComboUnlimited(277),
    //繼承人
    SoulExalt(278),
    //未知(未確定)
    IgnorePCounter(280),
    //靈魂深造
    IgnoreAllCounter(281),
    // 更新BUFF用
    IgnorePImmune(282),
    //隱‧鎖鏈地獄                  [確定完成]
    IgnoreAllImmune(279),
    //終極審判[猜測]
    FinalJudgement(283),
    // 更新BUFF用
    IDA_BUFF_284(284),
    //冰雪結界                       [完成]
    IceAura(285),
    //火靈結界[猜測]                [完成]
    FireAura(286),
    // 復仇天使                    [完成]
    VengeanceOfAngel(287),
    //天堂之門                       [完成]
    HeavensDoor(288),
    // 更新BUFF用                   [完成]
    Preparation(289),
    // 減少格擋率                    [完成]
    BullsEye(290),
    // 更新BUFF用                     [完成]
    IncEffectHPPotion(291),
    // 更新BUFF用                    [完成]
    IncEffectMPPotion(292),
    //出血毒素                        [完成]
    BleedingToxin(293),
    // 更新BUFF用                  [完成]
    IgnoreMobDamR(294),
    //修羅                         [完成]
    Asura(295),
    //
    IDA_BUFF_296(296),
    //翻轉硬幣                     [完成]
    FlipTheCoin(297),
    //統合能量                      []
    UnityOfPower(298),
    //暴能續發                    [完成]
    Stimulate(299),
    //IDA特殊Buff                  [完成]
    ReturnTeleport(300),
    //==========================Mask[9] - 10 - IDA[0x5]

    //功能不知道                    [完成]
    DropRIncrease(301),
    //功能不知道                   [完成]
    IgnoreMobpdpR(302),
    //BOSS傷害                     [完成]
    BdR(303),
    // 更新BUFF用                   [完成]
    CapDebuff(304),
    //超越                          [完成]
    Exceed(305),
    //急速療癒                       [完成]
    DiabolikRecovery(306),
    // 更新BUFF用                   [完成]
    FinalAttackProp(307),
    //超越_負載                     [完成]
    ExceedOverload(308),
    //超越_負載量                   [完成]
    OverloadCount(309),
    // 壓制砲擊/沉月-攻擊數量         [完成]
    BuckShot(310),
    // 更新BUFF用                 [完成]
    FireBomb(311),
    // 更新BUFF用                  [完成]
    HalfstatByDebuff(312),
    //傑諾能量                    [完成]
    SurplusSupply(313),
    //IDA特殊Buff                [完成]
    SetBaseDamage(314),
    //IDA BUFF列表更新用            [完成]
    EVAR(315),
    //傑諾飛行                      [完成]
    NewFlying(316),
    //阿瑪蘭斯發電機                [完成]
    AmaranthGenerator(317),
    //解多人Buff用的                [完成]
    OnCapsule(318),
    //元素： 風暴                   [完成]
    CygnusElementSkill(319),
    //開天闢地[猜測]            [完成]
    StrikerHyperElectric(320),
    // 更新BUFF用               [完成]
    EventPointAbsorb(321),
    // 更新BUFF用                  [完成]
    EventAssemble(322),
    //風暴使者                    [完成]
    StormBringer(323),
    //光之劍-命中提升             [完成]
    ACCR(324),
    //迴避提升                    [完成]
    DEXR(325),
    //阿爾法                    [完成]
    Albatross(326),
    //                            [完成]
    Translucence(327),
    //雙重力量 : 沉月/旭日         [完成]
    PoseType(328),
    //光之劍                      [完成]
    LightOfSpirit(329),
    //元素： 靈魂                 [完成]
    ElementSoul(330),
    //雙重力量 : 沉月/旭日          [完成]
    GlimmeringTime(331),
    // 更新BUFF用                  [完成]
    TrueSight(332),
    // 更新BUFF用                   [完成]
    SoulExplosion(333),
    //==========================Mask[10] - 11 - IDA[0x4]

    //靈魂球BUFF                     [完成]
    SoulMP(334),
    //靈魂BUFF                        [完成]
    FullSoulMP(335),
    //                               [完成]
    SoulSkillDamageUp(336),
    //元素衝擊                     [完成]
    ElementalCharge(337),
    // 復原                            [完成]
    Restoration(338),
    //十字深鎖鏈                     [完成]
    CrossOverChain(339),
    //騎士衝擊波                [完成]
    ChargeBuff(340),
    //重生                          [完成]
    Reincarnation(341),
    // 超衝擊防禦光環                  [完成]
    KnightsAura(342),
    //寒冰迅移                 [完成]
    ChillingStep(343),
    //[完成]
    DotBasedBuff(344),
    //祝福福音                  [完成]
    BlessEnsenble(345),
    // 更新BUFF用               [完成]
    ComboCostInc(346),
    //功能不知道                   [完成]
    ExtremeArchery(347),
    //IDA特殊Buff                  [完成]
    NaviFlying(348),
    // 魔幻箭筒 進階顫抖           [完成]
    QuiverCatridge(349),
    //
    AdvancedQuiver(350),
    //IDA移動Buff                   []
    UserControlMob(351),
    //IDA特殊Buff                 [猜測]
    ImmuneBarrier(352),
    // 壓制                        [猜測]
    ArmorPiercing(353),
    //時之威能                      [202]
    ZeroAuraStr(354),
    //聖靈神速                      [202]
    ZeroAuraSpd(355),
    // 更新BUFF用                   [猜測]
    CriticalGrowing(356),
    // 更新BUFF用                   []
    QuickDraw(357),
    // 更新BUFF用                   []
    BowMasterConcentration(358),
    // 更新BUFF用                   []
    TimeFastABuff(359),
    // 更新BUFF用                   []
    TimeFastBBuff(360),
    // 更新BUFF用                   []
    GatherDropR(361),
    // 大砲(95001002)               []
    AimBox2D(362),
    // 更新BUFF用                 [猜測]
    IncMonsterBattleCaptureRate(364),
    // 更新BUFF用                   []
    CursorSniping(364),
    // 更新BUFF用                    [猜測]
    DebuffTolerance(366),
    //==========================Mask[11] - 12 - IDA[0x3]

    //無視怪物傷害(重生的輪行蹤)        [202確定]
    DotHealHPPerSecond(367),
    DotHealMPPerSecond(368),
    //靈魂結界
    SpiritGuard(369),
    //死裡逃生                        []
    PreReviveOnce(368),
    //IDA特殊Buff                     [202]
    SetBaseDamageByBuff(371),
    // 更新BUFF用                   []
    LimitMP(372),
    // 更新BUFF用                   []
    ReflectDamR(373),
    // 更新BUFF用                   [202]
    ComboTempest(374),
    // 更新BUFF用                   []
    MHPCutR(373),
    // 更新BUFF用                   []
    MMPCutR(374),
    //IDA移動Buff                   []
    SelfWeakness(377),
    //元素 : 闇黑                   []
    ElementDarkness(376),
    // 本鳳凰                       [202]
    FlareTrick(379),
    //燃燒                          [202]
    Ember(380),
    // 更新BUFF用                   []
    Dominion(379),
    // 更新BUFF用                   []
    SiphonVitality(380),
    // 更新BUFF用                   []
    DarknessAscension(381),
    // 更新BUFF用                   []
    BossWaitingLinesBuff(382),
    // 更新BUFF用                   [202]
    DamageReduce(385),
    // 暗影僕從                     []
    ShadowServant(384),
    // 更新BUFF用                   []
    ShadowIllusion(385),
    //功能不知道                     [猜測]
    KnockBack(388),
    AddAttackCount(389),
    // 更新BUFF用                    [猜測]
    ComplusionSlant(390),
    //召喚美洲豹                    [猜測]
    JaguarSummoned(391),
    //自由精神                     [猜測]
    JaguarCount(392),
    //功能不知道                   [猜測]
    SSFShootingAttack(393),
    // 更新BUFF用                   [猜測]
    DevilCry(394),
    //功能不知道                   [猜測]
    ShieldAttack(395),
    //光環效果                     [202]
    BMageAura(396),
    //黑暗閃電                      [猜測]
    DarkLighting(397),
    //戰鬥精通                       [猜測]
    AttackCountX(398),
    //死神契約                      [202]
    BMageDeath(399),
    // 更新BUFF用                   []
    BombTime(400),
    // 更新BUFF用                   []
    NoDebuff(401),
    // 更新BUFF用                   []
    BattlePvPMikeShield(402),
    // 更新BUFF用                   []
    BattlePvPMikeBugle(403),
    //神盾系統                      []
    XenonAegisSystem(404),
    //索魂精通                      [確定]
    AngelicBursterSoulSeeker(405),
    //小狐仙                        []
    HiddenPossession(407),
    //暗影蝙蝠                      []
    NightWalkerBat(408),
    // 更新BUFF用                   []
    NightLordMark(408),
    //燎原之火                      []
    WizardIgnite(409),
    //花炎結界                      [猜測]
    FireBarrier(410),
    //影朋‧花狐                    []
    ChangeFoxMan(411),
    //
    IDA_BUFF_412(412),
    //
    IDA_BUFF_413(413),
    //                                   [猜測]
    IDA_BUFF_414(414),
    //危機繩索                      []
    AURA_BOOST(415),
    //拔刀術                         [猜測]
    HayatoStance(416),
    //拔刀術-新技體                [猜測]
    HayatoStanceBonus(417),
    //制敵之先                      [猜測]
    EyeForEye(418),
    //柳身                          []
    WillowDodge(410),
    //紫扇仰波                   [猜測]
    Shikigami(419),
    //武神招來_物理攻擊力            [202]
    HayatoPAD(420),
    //武神招來_最大HP%              [202]
    HayatoHPR(421),
    //武神招來_最大MP%              [202]
    HayatoMPR(422),
    //
    IDA_BUFF_423(423),
    //拔刀術                        []
    BATTOUJUTSU_STANCE(424),
    //==========================Mask[12] - 13 - IDA[0x2]

    // 425
    IDA_BUFF_425(425),
    //迅速                             [猜測]
    Jinsoku(426),
    //一閃                              [猜測]
    HayatoCr(427),
    //花狐的祝福                      [猜測]
    HakuBlessing(428),
    //結界‧桔梗 BOSS傷                  [猜測]
    HayatoBoss(429),
    //解多人Buff用的                    []
    PLAYERS_BUFF430(430),
    // 更新BUFF用                       []
    IDA_BUFF_431(431),
    // 結界‧破魔                      [猜測]
    BlackHeartedCurse(432),
    // 更新BUFF用                       []
    IDA_BUFF_433(433),
    // 更新BUFF用                       []
    IDA_BUFF_434(434),
    //精靈召喚模式                      []
    AnimalChange(435),
    // 更新BUFF用                       []
    IDA_BUFF_436(436),
    // 更新BUFF用                       []
    IDA_BUFF_437(437),
    // 更新BUFF用                       []
    IDA_BUFF_438(438),
    // 更新BUFF用                       []
    IDA_BUFF_439(439),
    //IDA特殊Buff                       []
    IDA_SPECIAL_BUFF_4(440),
    // 更新BUFF用                       []
    IDA_BUFF_441(441),
    // 更新BUFF用                       []
    IDA_BUFF_442(442),
    //==========================Mask[13] - 14 - IDA[0x0]

    //能量獲得                           [202]
    BattlePvPHelenaMark(443),
    // 更新BUFF用                     [猜測]
    BattlePvPHelenaWindSpirit(444),
    //預設Buff-3                       [猜測]
    BattlePvPLangEProtection(445),
    // 更新BUFF用                     [猜測]
    BattlePvPLeeMalNyunScaleUp(446),
    // 更新BUFF用                       [猜測]
    BattlePvPRevive(447),
    //皮卡啾攻擊                      [猜測]
    PinkbeanAttackBuff(448),
    //皮卡啾未知                        []
    PinkbeanRelax(449),
    //預設Buff-4                     [猜測]
    PinkbeanRollingGrade(450),
    //烈焰溜溜球                        []
    PinkbeanYoYoStack(451),
    //曉月流基本技_提升攻擊力#damR%      []
    RandAreaAttack(452),
    // 更新BUFF用                       []
    NextAttackEnhance(453),
    // 更新BUFF用                       []
    AranBeyonderDamAbsorb(454),
    // 更新BUFF用                     []
    AranCombotempastOption(455),
    // 更新BUFF用                     []
    NautilusFinalAttack(456),
    // 更新BUFF用                     []
    ViperTimeLeap(457),
    // 更新BUFF用                      [猜測]
    RoyalGuardState(458),
    // 更新BUFF用                  [猜測]
    RoyalGuardPrepare(459),
    // 更新BUFF用                   [202]
    MichaelSoulLink(460),
    // 更新BUFF用                   [猜測]
    MichaelStanceLink(461),
    // 更新BUFF用                      [猜測]
    TriflingWhimOnOff(462),
    // 更新BUFF用                  [猜測]
    AddRangeOnOff(463),
    // ESP數量                   [猜測]
    KinesisPsychicPoint(464),
    // 更新BUFF用                  [猜測]
    KinesisPsychicOver(465),
    // 更新BUFF用                     [猜測]
    KinesisPsychicShield(466),
    // 更新BUFF用                   [猜測]
    KinesisIncMastery(467),
    // 更新BUFF用                     []
    KinesisPsychicEnergeShield(468),
    // 更新BUFF用                     [猜測]
    BladeStance(469),
    // 更新BUFF用                     []
    DebuffActiveSkillHPCon(461),
    // 更新BUFF用                     []
    DebuffIncHP(462),
    // 更新BUFF用                     []
    BowMasterMortalBlow(463),
    // 更新BUFF用                     []
    AngelicBursterSoulResonance(464),
    // 更新BUFF用                     [猜測]
    Fever(474),
    // //依古尼斯咆哮                  [確定]
    IgnisRore(475),
    // 更新BUFF用                     []
    RpSiksin(476),
    // 更新BUFF用                     []
    TeleportMasteryRange(477),
    // 更新BUFF用                     []
    FixCoolTime(478),
    // 更新BUFF用                     []
    IncMobRateDummy(479),
    // 更新BUFF用                    [猜測]
    AdrenalinBoost(480),
    // 更新BUFF用                 [猜測]
    AranSmashSwing(481),
    //吸血術                   [確定完成]
    AranDrain(482),
    // 更新BUFF用                 [猜測]
    AranBoostEndHunt(483),
    // 更新BUFF用                    [猜測]
    HiddenHyperLinkMaximization(485),
    // 更新BUFF用                       [猜測]
    RWCylinder(486),

    RWCombination(487),
    // 更新BUFF用                      [猜測]
    RWMagnumBlow(488),
    // 更新BUFF用                      [猜測]
    RWBarrier(489),
    // 更新BUFF用                     [猜測]
    RWBarrierHeal(490),
    // 更新BUFF用                  [猜測]
    RWMaximizeCannon(491),
    // 更新BUFF用                       [猜測]
    RWOverHeat(492),
    // 更新BUFF用                       [猜測]
    UsingScouter(493),
    // 更新BUFF用                     [猜測]
    RWMovingEvar(494),
    // 更新BUFF用                     [202]
    Stigma(495),
    // 更新BUFF用                     []
    IDA_BUFF_495(495),
    // 更新BUFF用                     []
    IDA_BUFF_496(496),
    // 更新BUFF用                     []
    IDA_BUFF_497(488),
    // 更新BUFF用                     []
    IDA_BUFF_489(489),
    // 更新BUFF用                     []
    IDA_BUFF_490(490),
    // 更新BUFF用                     []
    IDA_BUFF_491(491),
    // 更新BUFF用                     []
    元氣覺醒(502),
    // 更新BUFF用                     []
    能量爆炸(503),
    // 更新BUFF用                     []
    IDA_BUFF_504(504),
    // 更新BUFF用                     []
    IDA_BUFF_505(505),
    // 更新BUFF用                     []
    IDA_BUFF_506(506),
    // 更新BUFF用                     []
    交換攻擊(507),
    // 更新BUFF用                     []
    聖靈祈禱(508),
    // 更新BUFF用                     []
    IDA_BUFF_509(509),
    // 更新BUFF用                     []
    IDA_BUFF_510(510),
    // 更新BUFF用                     []
    IDA_BUFF_511(511),
    // 更新BUFF用                     []
    散式投擲(512),
    // 更新BUFF用                     []
    IDA_BUFF_513(513),
    // 更新BUFF用                     []
    IDA_BUFF_514(514),
    // 更新BUFF用                     []
    滅殺刃影(515),
    // 更新BUFF用                     []
    狂風呼嘯(516),
    // 更新BUFF用                     []
    IDA_BUFF_517(517),
    // 更新BUFF用                     [猜測]
    惡魔狂亂(518),
    // 更新BUFF用                     []
    IDA_BUFF_519(519),
    // 更新BUFF用                     []
    IDA_BUFF_520(520),
    // 更新BUFF用                     []
    IDA_BUFF_521(521),
    // 更新BUFF用                     []
    IDA_BUFF_522(522),
    // 更新BUFF用                     []
    IDA_BUFF_523(523),
    // 更新BUFF用                     []
    IDA_BUFF_524(524),
    // 更新BUFF用                     []
    IDA_BUFF_525(525),
    // 更新BUFF用                     []
    IDA_BUFF_526(526),
    // 更新BUFF用                     []
    IDA_BUFF_527(527),
    // 更新BUFF用                     []
    IDA_BUFF_528(528),
    // 更新BUFF用                     []
    IDA_BUFF_529(529),
    // 更新BUFF用                     []
    IDA_BUFF_530(530),
    // 更新BUFF用                     []
    IDA_BUFF_531(531),
    // 更新BUFF用                     []
    IDA_BUFF_532(532),
    // 更新BUFF用                     []
    IDA_BUFF_533(533),
    // 更新BUFF用                     []
    IDA_BUFF_534(534),
    // 更新BUFF用                     []
    IDA_BUFF_535(535),
    // 更新BUFF用                     []
    IDA_BUFF_536(536),
    // 更新BUFF用                     []
    IDA_BUFF_537(537),
    // 更新BUFF用                     []
    IDA_BUFF_538(538),
    // 更新BUFF用                     []
    IDA_BUFF_539(539),
    // 更新BUFF用                     []
    IDA_BUFF_540(540),
    // 更新BUFF用                     []
    IDA_BUFF_541(541),
    // 更新BUFF用                     []
    IDA_BUFF_542(542),
    // 更新BUFF用                     []
    IDA_BUFF_543(543),
    // 更新BUFF用                     []
    IDA_BUFF_544(544),
    IDA_BUFF_545(545),
    //預設Buff-5                        []
    EnergyCharged(546),
    //衝鋒_速度                         [確定完成]
    DashSpeed(547),
    //衝鋒_跳躍                         [確定完成]
    DashJump(548),
    //怪物騎乘                         [確定完成]
    RideVehicle(549),
    //最終極速                          [確定完成]
    PartyBooster(550),
    //指定攻擊(無盡追擊)                [確定完成]
    GuidedBullet(551),
    //預設Buff-1                         [確定完成]
    Undead(552),
    //預設Buff-2                        []
    RideVehicleExpire(553),
    // 更新BUFF用                     []
    COUNT_PLUS1(554),
    // 更新BUFF用                     []
    IDA_BUFF_555(555),
    // 更新BUFF用                     []
    IDA_BUFF_556(556),
    // 更新BUFF用                     []
    IDA_BUFF_557(557),
    // 更新BUFF用                     []
    IDA_BUFF_558(558),
    // 更新BUFF用                     []
    IDA_BUFF_559(559),
    // 更新BUFF用                     []
    IDA_BUFF_560(560),
    // 更新BUFF用                     []
    IDA_BUFF_561(561),
    // 更新BUFF用                     []
    NONE_END(562),
    NONE(-1),
    //TODO: 召喚獸
    SUMMON(51, true),
    //-----------------[已停用的Buff]
    //黑色繩索
    DARK_AURA_OLD(888),
    //藍色繩索
    BLUE_AURA_OLD(888),
    //黃色繩索
    YELLOW_AURA_OLD(888),
    //貓頭鷹召喚
    OWL_SPIRIT(888),
    //超級體
    BODY_BOOST(888),;
    public static final int SIZE = 18;
    private static final long serialVersionUID = 0L;
    private final int nValue;
    private final int nPos;
    private boolean isIndie = false;

    private CharacterTemporaryStat(int nValue) {
        this.nValue = 1 << (31 - (nValue % 32));
        this.nPos = (int) Math.floor(nValue / 32);
    }

    private CharacterTemporaryStat(int nValue, boolean isIndie) {
        this.nValue = 1 << (31 - (nValue % 32));
        this.nPos = (int) Math.floor(nValue / 32);
        this.isIndie = isIndie;
    }

    public static CharacterTemporaryStat getCharacterTemporaryStat(int buff) {
        int buf = 1 << (31 - (buff % 32));
        int fir = (int) Math.floor(buff / 32);
        for (CharacterTemporaryStat bb : values()) {
            if (bb.nValue == buf && bb.nPos == fir) {
                return bb;
            }
        }
        return CharacterTemporaryStat.IndiePAD;
    }

    public static boolean isEncode4Bytes(CharacterTemporaryStat stat) {
        switch (stat) {
            case CarnivalDefence:
            case SpiritLink:
            case DojangLuckyBonus:
            case SoulGazeCriDamR:
            case PowerTransferGauge:
            case ReturnTeleport:
            case ShadowPartner:
            case IncMaxDamage:
//            case Cyclone:
//            case IDA_SPECIAL_BUFF_4:
            case SetBaseDamage:
            case NaviFlying:
//            case ImmuneBarrier:
            case QuiverCatridge:
            case Dance:
            case SetBaseDamageByBuff:
            case EMHP:
            case EMMP:
            case EPAD:
            case EPDD:
            case DotHealHPPerSecond:
            case DotHealMPPerSecond:
            case MagnetArea:
            case VampDeath:
            case IDA_BUFF_532:
            case IDA_BUFF_296:
            case IDA_BUFF_539:
            case IDA_BUFF_412:
            case IDA_BUFF_157:
                return true;
        }
        return false;
    }

    public int getPosition() {
        return nPos;
    }

    public int getValue() {
        return nValue;
    }

    public final boolean isIndie() {
        return isIndie;
    }

    public boolean isMovingEffectingStat() {
        switch (this) {
            case Speed:
            case Jump:
            case Stun:
            case Weakness:
            case Slow:
            case Morph:
            case Ghost:
            case BasicStatUp:
            case Attract:
            case RideVehicle:
            case DashSpeed:
            case DashJump:
            case Flying:
            case Frozen:
            case Frozen2:
            case Lapidification:
            case IndieSpeed:
            case IndieJump:
            case KeyDownMoving:
            case EnergyCharged:
            case Mechanic:
            case Magnet:
            case MagnetArea:
            case VampDeath:
            case VampDeathSummon:
            case GiveMeHeal:
            case PvPScoreBonus:
            case NewFlying:
            case QuiverCatridge:
            case UserControlMob:
            case Dance:
                /**
                 * *
                 *
                 */
            case SelfWeakness:
            case Undead:
            case IDA_SPECIAL_BUFF_4:
            case BattlePvPHelenaWindSpirit:
            case BattlePvPLeeMalNyunScaleUp:
            case TouchMe:
                return true;
        }
        return false;
    }
}
