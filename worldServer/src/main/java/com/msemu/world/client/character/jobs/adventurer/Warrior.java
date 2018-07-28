package com.msemu.world.client.character.jobs.adventurer;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.packets.outpacket.mob.LP_MobStatSet;
import com.msemu.core.network.packets.outpacket.summon.LP_SummonLeaveField;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatReset;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.*;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.Summon;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.LeaveType;
import com.msemu.world.enums.MoveAbility;
import com.msemu.world.enums.Stat;

import java.util.Arrays;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.stats.CharacterTemporaryStat.*;

/**
 * Created by Weber on 2018/5/18.
 */
public class Warrior extends JobHandler {

    public static final int 回歸楓之谷 = 1281;

    //Hero
    public static final int 極速武器 = 1101004;
    public static final int 鬥氣集中 = 1101013;
    public static final int 激勵 = 1101006;
    public static final int FINAL_ATTACK_FIGHTER = 1100002;
    public static final int FINAL_ATTACK_PAGE = 1200002;
    public static final int FINAL_ATTACK_SPEARMAN = 1300002;
    public static final int 楓葉祝福_英雄 = 1121000;
    public static final int 極速武器_見習騎士 = 1201004;
    public static final int COMBO_FURY = 1101012;
    public static final int COMBO_FURY_DOWN = 1100012;
    public static final int 黑暗之劍 = 1111003;
    public static final int 虎咆哮 = 1111008;
    public static final int 虎咆哮_下 = 1111014;
    public static final int 進階終極攻擊_英雄 = 1120013;
    public static final int 鬥氣爆發 = 1121010;
    public static final int 烈焰翔斬 = 1121015;
    public static final int 魔防消除_英雄 = 1121016;
    public static final int 鬥氣綜合 = 1110013;
    public static final int 進階鬥氣 = 1120003;

    //Paladin
    public static final int 騎士密令 = 1201013;
    public static final int 元素衝擊 = 1200014;
    public static final int 烈焰之劍 = 1201011;
    public static final int 寒冰之劍 = 1201012;
    public static final int 雷鳴之劍 = 1211008;
    public static final int 復原 = 1211010;
    public static final int 戰鬥命令 = 1211011;
    public static final int 超衝擊防禦 = 1211014;
    public static final int 聖靈之劍 = 1221004;
    public static final int 降魔咒 = 1211013;
    public static final int 自然之力 = 1221015;
    public static final int 楓葉祝福_聖騎士 = 1221000;
    public static final int 守護者精神 = 1221016;
    public static final int 騎士衝擊波 = 1221009;
    public static final int 祝福護甲 = 1210016;
    public static final int 魔防消除_聖騎士 = 1221014;

    //Dark Knight
    public static final int SPEAR_SWEEP = 1301012;
    public static final int 極速武器_槍騎士 = 1301004;
    public static final int 禦魔陣 = 1301006;
    public static final int 神聖之火 = 1301007;
    public static final int 追隨者 = 1301013;
    public static final int EVIL_EYE_OF_DOMINATION = 1311013; //Beholder TSM
    public static final int 十字深鎖鏈 = 1311015;
    public static final int 暗黑之力 = 1310009;
    public static final int 楓葉祝福_黑騎士 = 1321000;
    public static final int 轉生 = 1320016;
    public static final int 魔防消除_黑騎士 = 1321014;
    public static final int 暗之獻祭 = 1321015; //Resets summon


    //Hyper Skills
    public static final int 傳說冒險_英雄 = 1121053; //Lv200
    public static final int 傳說冒險_聖騎士 = 1221053; //Lv200
    public static final int 傳說冒險_黑騎士 = 1321053; //Lv200
    public static final int 劍士意念 = 1121054; //Lv150
    public static final int 神域護佑 = 1221054; //Lv150
    public static final int 黑暗飢渴 = 1321054; //Lv150
    public static final int 神之滅擊 = 1221052; //Lv170

    private Summon evilEye;

    private int[] addedSkills = new int[]{
            回歸楓之谷,
    };


    private final int[] buffs = new int[]{
            極速武器, // Weapon Booster - Fighter
            鬥氣集中, // Combo Attack
            激勵, // Rage
            楓葉祝福_英雄, // Maple Warrior
            楓葉祝福_聖騎士,
            楓葉祝福_黑騎士,
            極速武器_見習騎士, // Weapon Booster - Page
            極速武器_槍騎士,
            戰鬥命令,
            超衝擊防禦,
            自然之力,
            守護者精神,
            追隨者,
            禦魔陣,
            神聖之火,
            十字深鎖鏈,
            騎士衝擊波,
            鬥氣爆發,
            暗之獻祭,

            傳說冒險_黑騎士,
            傳說冒險_英雄,
            傳說冒險_聖騎士,
            劍士意念,
            神域護佑,
            黑暗飢渴,
    };
    private long lastPanicHit = Long.MIN_VALUE;
    private long lastHpRecovery = Long.MIN_VALUE;
    private int lastCharge = 0;
    private int recoveryAmount = 0;
    private int divShieldAmount = 0;


    public Warrior(Character character) {
        super(character);
        if (isHandlerOfJob(getCharacter().getJob())) {
            for (int skillID : addedSkills) {
                if (!getCharacter().hasSkill(skillID)) {
                    Skill skill = SkillData.getInstance().getSkillById(skillID);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    getCharacter().addSkill(skill);
                }
            }
        }
    }

    public void handleBuff(SkillUseInfo skillUseInfo) {
        final int skillId = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = getSkillInfo(skillId);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillId) {
            case 極速武器:
            case 極速武器_見習騎士:
            case 極速武器_槍騎士:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case 激勵:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o2);
                break;
            case 鬥氣集中:
                o1.nOption = 1;
                o1.rOption = skillId;
                o1.tOption = 0;
                tsm.putCharacterStatValue(ComboCounter, o1);
                break;
            case 鬥氣爆發:
                removeCombo(1);
                o1.nOption = 1;
                o1.rOption = skillId;
                tsm.putCharacterStatValue(Enrage, o1); // max mobs hit
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillId;
                tsm.putCharacterStatValue(EnrageCrDamMin, o2);
                o3.nOption = si.getValue(x, slv);
                o2.rOption = skillId;
                tsm.putCharacterStatValue(EnrageCr, o3);
                break;
            case 戰鬥命令:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CombatOrders, o1);
                break;
            case 超衝擊防禦:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nReason = skillId;
                o2.nValue = si.getValue(indiePddR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePDDR, o1);
                o3.nOption = si.getValue(z, slv);
                o3.rOption = skillId;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Guard, o3);
                o4.nOption = 1;
                o4.rOption = skillId;
                o4.tOption = 0;
                tsm.putCharacterStatValue(KnightsAura, o4);
                break;
            case 自然之力:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                break;
            case 守護者精神:
                o1.nOption = 1;
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                break;
            case 禦魔陣:
                o1.nOption = si.getValue(pdd, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o1);
                break;
            case 神聖之火:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(MaxHP, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(MaxMP, o2);
                break;
            case 十字深鎖鏈:
                int total = chr.getStat(Stat.MAX_HP);
                int current = chr.getStat(Stat.HP);
                o1.nOption = (int) ((si.getValue(x, slv) * ((double) current) / total) * 100);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nOption = (int) Math.min((0.08 * total - current), si.getValue(z, slv));
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o2);
                break;
            case 追隨者:
                spawnEvilEye(skillId, slv);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o2);
                o3.nOption = 1;
                o3.rOption = skillId;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Beholder, o1);
                break;
            case 暗之獻祭:
                if (tsm.hasStat(Beholder)) {
                    o1.nOption = si.getValue(y, slv);
                    o1.rOption = skillId;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(Restoration, o1);
                    o2.nOption = si.getValue(x, slv);
                    o2.rOption = skillId;
                    o2.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IgnoreMobpdpR, o2);
                    o3.nOption = si.getValue(indieBDR, slv);
                    o3.rOption = skillId;
                    o3.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieBDR, o3);
                    removeEvilEye();
                }
                break;
            case 楓葉祝福_英雄:
            case 楓葉祝福_聖騎士:
            case 楓葉祝福_黑騎士:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BasicStatUp, o1);
                break;
            case 騎士衝擊波:
                o1.nOption = si.getValue(cr, slv);
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o1);
                o2.nOption = si.getValue(damR, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o2);
                o3.nOption = si.getValue(ignoreMobpdpR, slv);
                o3.rOption = skillId;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IgnoreMobpdpR, o3);
                break;

            //Hypers
            case 傳說冒險_英雄:
            case 傳說冒險_聖騎士:
            case 傳說冒險_黑騎士:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillId;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
            case 神域護佑:
                o1.nOption = 1;
                o1.rOption = skillId;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                break;
            case 劍士意念:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indieCr, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o2);
                tsm.putCharacterStatValue(TerR, o2);
                break;
            case 黑暗飢渴:
                o1.nReason = skillId;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillId;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DotHealHPPerSecond, o2); //TODO   ?  unsure about TempStat
                break;
        }
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    @Override
    public boolean handleAttack(AttackInfo attackInfo) {
        final boolean normalAttack = attackInfo.skillId == 0;
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final Skill skill = chr.getSkill(attackInfo.skillId);
        final int skillID = skill != null ? skill.getSkillId() : 0;
        SkillInfo si = skill != null ? getSkillInfo(skillID) : null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = attackInfo.getSlv();
        if ((!normalAttack && (skill == null || si == null)))
            return false;
        if ((!normalAttack) && skill.getCurrentLevel() != slv)
            return false;
        if (normalAttack && slv != 0)
            return false;
        // trigger cheat attack
        final int comboProp = getComboProp();
        if (chr.getJob() >= MapleJob.狂戰士.getId() && chr.getJob() <= MapleJob.英雄.getId()) {
            if (hasHitMobs) {
                //Combo
                if (Rand.getChance(comboProp)) {
                    addCombo();
                    Skill advCombo = chr.getSkill(鬥氣集中);
                    int secondProp = SkillData.getInstance().getSkillInfoById(advCombo.getSkillId()).getValue(prop, slv);
                    if (Rand.getChance(secondProp)) {
                        addCombo();
                    }
                }

            }
        }

        if (chr.getJob() >= MapleJob.騎士.getId() && chr.getJob() <= MapleJob.聖騎士.getId()) {
            if (hasHitMobs) {
                int x = 1;
            }
        }

        if (chr.getJob() >= MapleJob.槍騎兵.getId() && chr.getJob() <= MapleJob.黑騎士.getId()) {
            if (hasHitMobs) {
                int x = 1;
            }
        }

        if (normalAttack) return true;

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();


        switch (skillID) {
            case COMBO_FURY:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (Rand.getChance(si.getValue(prop, skill.getCurrentLevel()))) {
                        if (!mob.isBoss()) {
                            o1.nOption = 1;
                            o1.rOption = skill.getSkillId();
                            o1.tOption = si.getValue(time, skill.getCurrentLevel());
                            mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                        }
                        addCombo();
                    }
                }
                break;
            case COMBO_FURY_DOWN:
                if (hasHitMobs) {
                    removeCombo(1);
                }
                break;
            case 黑暗之劍:
                if (hasHitMobs) {
                    removeCombo(2);
                    int allowedTime = si.getValue(subTime, slv);
                    if (lastPanicHit + allowedTime * 1000 > System.currentTimeMillis()) {
                        removeCombo(1);
                    }
                    lastPanicHit = System.currentTimeMillis();
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        lastPanicHit = System.currentTimeMillis();
                        o1.nOption = si.getValue(z, slv);
                        o1.rOption = skill.getSkillId();
                        o1.tOption = 0;
                        mts.addStatOptions(MobBuffStat.PAD, o1);
                        if (Rand.getChance(si.getValue(prop, slv))) {
                            o2.nOption = -si.getValue(x, slv); // minus?
                            o2.rOption = skill.getSkillId();
                            o2.tOption = si.getValue(time, slv);
                            mts.addStatOptions(MobBuffStat.ACC, o2);
                        }
                        getClient().write(new LP_MobStatSet(mob, (short) 0));
                    }
                }
                break;
            case 虎咆哮_下:
                Skill orig = chr.getSkill(虎咆哮);
                slv = orig.getCurrentLevel();
                si = SkillData.getInstance().getSkillInfoById(虎咆哮_下);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    removeCombo(1);
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (mob.isBoss()) {
                        o1.nOption = si.getValue(x, slv);
                        o1.rOption = 虎咆哮_下;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Weakness, o1);
                    } else {
                        o1.nOption = 1;
                        o1.rOption = 虎咆哮_下;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 烈焰翔斬:
                if (hasHitMobs) {
                    removeCombo(si.getValue(y, slv));
                }
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = si.getValue(y, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptions(MobBuffStat.AddDamParty, o1);
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                }
                break;
            case 騎士密令:
                if (Rand.getChance(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 烈焰之劍:
                handleCharges(skill.getSkillId());
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                }
                break;
            case 寒冰之劍:
                handleCharges(skill.getSkillId());
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Speed, o1);
                    }
                }
                break;
            case 雷鳴之劍:
                handleCharges(skill.getSkillId());
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    } else {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                }
                break;
            case 聖靈之劍:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Seal, o1);
                    }
                }
                handleCharges(skill.getSkillId());
                break;
            case 騎士衝擊波:
                int charges = tsm.getOption(ElementalCharge).mOption;
                if (charges == SkillData.getInstance().getSkillInfoById(元素衝擊).getValue(z, 1)) {
                    if (tsm.getOptByCTSAndSkill(DamR, 騎士衝擊波) == null) {
                        resetCharges();
                        int t = si.getValue(time, slv);
                        o1.nOption = si.getValue(cr, slv);
                        o1.rOption = skillID;
                        o1.tOption = t;
                        tsm.putCharacterStatValue(CriticalBuff, o1);
                        o2.nOption = si.getValue(ignoreMobpdpR, slv);
                        o2.rOption = skillID;
                        o2.tOption = t;
                        tsm.putCharacterStatValue(IgnoreMobpdpR, o2);
                        o3.nOption = si.getValue(damR, slv);
                        o3.rOption = skillID;
                        o3.tOption = t;
                        tsm.putCharacterStatValue(DamR, o3);
                        getClient().write(new LP_TemporaryStatSet(tsm));
                    }
                }
                break;
            case 神之滅擊:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Smite, o1);
                }
                break;
            case SPEAR_SWEEP:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                }
                break;
//            case FINAL_ATTACK_FIGHTER:
//            case FINAL_ATTACK_SPEARMAN:
//            case FINAL_ATTACK_PAGE:
//                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
//                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
//                    long dmg = 0;
//                    for (int i = 0; i < mai.getDamages().length; i++) {
//                        dmg += mai.getDamages()[i];
//                    }
//                    c.write(MobPool.mobDamaged(mob.getObjectId(),dmg, mob.getTemplateId(), (byte) 1,(int)  mob.getHp(), (int) mob.getMaxHp()));
//                }
        }
        return true;
    }

    @Override
    public boolean handleSkillUse(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final Skill skill = chr.getSkill(skillID);
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = skill != null ? getSkillInfo(skillID) : null;
        if (si == null) {
            return false;
        }
        if (isBuff(skillID)) {
            handleBuff(skillUseInfo);
        } else {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            switch (skillID) {
                case 回歸楓之谷:
                    o1.nValue = si.getValue(x, slv);
                    Field toField = getClient().getChannelInstance().getField(o1.nValue);
                    chr.warp(toField);
                    break;
                case 復原:
                    int t = 1000 * si.getValue(time, slv);
                    long cur = System.currentTimeMillis();
                    if (lastHpRecovery + t < cur) {
                        recoveryAmount = si.getValue(x, slv);
                    } else {
                        recoveryAmount = Math.max(si.getValue(y, slv), (int) (recoveryAmount * (si.getValue(z, slv) / 100D)));
                    }
                    lastHpRecovery = cur;
                    break;
                case 降魔咒:
//                    Rect rect = new Rect(inPacket.decodeShort(), inPacket.decodeShort()
//                            , inPacket.decodeShort(), inPacket.decodeShort());
//                    chr.getField().getMobInRect(rect).stream().filter(mob -> mob.getHp() > 0).forEach(mob -> {
//                        MobTemporaryStat mts = mob.getTemporaryStat();
//                        if (Rand.getChance(si.getValue(prop, slv))) {
//                            o1.nOption = si.getValue(x, slv);
//                            o1.rOption = skillID;
//                            o1.tOption = si.getValue(time, slv);
//                            mts.addStatOptions(MobBuffStat.PAD, o1);
//                            mts.addStatOptions(MobBuffStat.MAD, o1);
//                            mts.addStatOptions(MobBuffStat.PDR, o1);
//                            mts.addStatOptions(MobBuffStat.MDR, o1);
//                            o2.nOption = -si.getValue(z, slv);
//                            o2.rOption = skillID;
//                            o2.tOption = si.getValue(subTime, slv);
//                            mts.addStatOptionsAndBroadcast(MobBuffStat.Darkness, o2);
//                        }
//                    });
                    break;
                case 魔防消除_黑騎士:
                case 魔防消除_英雄:
                case 魔防消除_聖騎士:
//                    Rect rect2 = new Rect(inPacket.decodeShort(), inPacket.decodeShort()
//                            , inPacket.decodeShort(), inPacket.decodeShort());
//                    chr.getField().getMobInRect(rect2).stream().filter(mob -> mob.getHp() > 0).forEach(mob -> {
//                        MobTemporaryStat mts = mob.getTemporaryStat();
//                        if (Rand.getChance(si.getValue(prop, slv))) {
//                            o1.nOption = 1;
//                            o1.rOption = skillID;
//                            o1.tOption = si.getValue(time, slv);
//                            mts.addStatOptionsAndBroadcast(MobBuffStat.MagicCrash, o1);
//                        }
//                    });
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        MapleJob job = MapleJob.getById(id);
        switch (job) {
            case 劍士:
            case 狂戰士:
            case 十字軍:
            case 英雄:
            case 見習騎士:
            case 騎士:
            case 聖騎士:
            case 槍騎兵:
            case 嗜血狂騎:
            case 黑騎士:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getFinalAttackSkill() {
        Character chr = getCharacter();
        if (Rand.getChance(getFinalAttackProp())) {
            int skillID = 0;
            if (chr.hasSkill(FINAL_ATTACK_FIGHTER)) {
                skillID = FINAL_ATTACK_FIGHTER;
            }
            if (chr.hasSkill(FINAL_ATTACK_PAGE)) {
                skillID = FINAL_ATTACK_PAGE;
            }
            if (chr.hasSkill(FINAL_ATTACK_SPEARMAN)) {
                skillID = FINAL_ATTACK_SPEARMAN;
            }
            if (chr.hasSkill(進階終極攻擊_英雄)) {
                skillID = 進階終極攻擊_英雄;
            }
            return skillID;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isBuff(int skillID) {
        return Arrays.stream(buffs).anyMatch(b -> b == skillID);
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {
        Character chr = getCharacter();
        if (chr.hasSkill(祝福護甲)) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            SkillInfo si = SkillData.getInstance().getSkillInfoById(祝福護甲);
            int slv = si.getCurrentLevel();
            int shieldProp = si.getValue(SkillStat.prop, slv);       //TODO should be prop in WzFiles, but it's actually 0
            Option o1 = new Option();
            Option o2 = new Option();
            if (tsm.hasStat(BlessingArmor)) {
                if (divShieldAmount < 10) {
                    divShieldAmount++;
                } else {
                    resetDivineShield();
                    divShieldAmount = 0;
                }
            } else {
                if (Rand.getChance(shieldProp)) {
                    o1.nOption = 1;
                    o1.rOption = 祝福護甲;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(BlessingArmor, o1);
                    o2.nOption = si.getValue(epad, slv);
                    o2.rOption = 祝福護甲;
                    o2.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(PAD, o2);
                    getClient().write(new LP_TemporaryStatSet(tsm));
                    divShieldAmount = 0;
                }
            }
        }
        if (chr.hasSkill(鬥氣綜合)) {
            SkillInfo csi = SkillData.getInstance().getSkillInfoById(鬥氣綜合);
            int slv = csi.getCurrentLevel();
            int comboProp = csi.getValue(subProp, slv);
            if (Rand.getChance(comboProp)) {
                addCombo();
            }
        }
    }

    private int getComboProp() {
        Skill skill = null;
        if (getCharacter().hasSkill(鬥氣綜合)) {    //Combo Synergy
            skill = getCharacter().getSkill(鬥氣綜合);
        } else if (getCharacter().hasSkill(鬥氣集中)) {
            skill = getCharacter().getSkill(鬥氣集中);
        }
        if (skill == null) {
            return 0;
        }
        return SkillData.getInstance().getSkillInfoById(skill.getSkillId()).getValue(prop, skill.getCurrentLevel());
    }

    private int getComboCount() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        if (tsm.hasStat(ComboCounter)) {
            return tsm.getOption(ComboCounter).nOption;
        }
        return -1;
    }

    private void addCombo() {
        int currentCount = getComboCount();
        if (currentCount < 0) {
            return;
        }
        if (currentCount < getMaxCombo()) {
            Option o = new Option();
            o.nOption = currentCount + 1;
            o.rOption = 鬥氣集中;
            getCharacter().getTemporaryStatManager().putCharacterStatValue(ComboCounter, o);
            getCharacter().getClient().write(new LP_TemporaryStatSet(getCharacter().getTemporaryStatManager()));
        }
    }

    private void removeCombo(int count) {
        int currentCount = getComboCount();
        Option o = new Option();
        if (currentCount > count + 1) {
            o.nOption = currentCount - count;
        } else {
            o.nOption = 0;
        }
        o.rOption = 鬥氣集中;
        getCharacter().getTemporaryStatManager().putCharacterStatValue(ComboCounter, o);
        getCharacter().getClient().write(new LP_TemporaryStatSet(getCharacter().getTemporaryStatManager()));
    }

    private int getMaxCombo() {
        int num = 0;
        if (getCharacter().hasSkill(鬥氣集中)) {
            num = 6;
        }
        if (getCharacter().hasSkill(進階鬥氣)) {
            num = 11;
        }
        return num;
    }

    private void resetCharges() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        tsm.removeStat(ElementalCharge, false);
        getClient().write(new LP_TemporaryStatReset(tsm, false));
    }

    private void handleCharges(int skillId) {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option option = new Option();
        SkillInfo chargeInfo = SkillData.getInstance().getSkillInfoById(元素衝擊);
        int amount = 1;
        if (tsm.hasStat(ElementalCharge)) {
            amount = tsm.getOption(ElementalCharge).mOption;
            if (lastCharge == skillId) {
                return;
            }
            if (amount < chargeInfo.getValue(z, 1)) {
                amount++;
            }
        }
        lastCharge = skillId;
        option.nOption = 1;
        option.rOption = 元素衝擊;
        option.tOption = 10 * chargeInfo.getValue(time, 1); // elemental charge  // 10x actual duration
        option.mOption = amount;
        option.wOption = amount * chargeInfo.getValue(w, 1); // elemental charge
        option.uOption = amount * chargeInfo.getValue(u, 1);
        option.zOption = amount * chargeInfo.getValue(z, 1);
        tsm.putCharacterStatValue(ElementalCharge, option);
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private Skill getFinalAtkSkill() {
        Character chr = getCharacter();
        Skill skill = null;
        if (chr.hasSkill(FINAL_ATTACK_FIGHTER)) {
            skill = chr.getSkill(FINAL_ATTACK_FIGHTER);
        }
        if (chr.hasSkill(FINAL_ATTACK_PAGE)) {
            skill = chr.getSkill(FINAL_ATTACK_PAGE);
        }
        if (chr.hasSkill(FINAL_ATTACK_SPEARMAN)) {
            skill = chr.getSkill(FINAL_ATTACK_SPEARMAN);
        }

        if (chr.hasSkill(進階終極攻擊_英雄)) {
            skill = chr.getSkill(進階終極攻擊_英雄);
        }
        return skill;
    }

    private int getFinalAttackProp() {
        Character chr = getCharacter();
        Skill skill = getFinalAtkSkill();
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
        byte slv = (byte) chr.getSkill(skill.getSkillId()).getCurrentLevel();
        return si.getValue(prop, slv);
    }

    private void resetDivineShield() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        tsm.removeStat(BlessingArmor, false);
        tsm.removeStat(PAD, false);
        getClient().write(new LP_TemporaryStatReset(tsm, false));
    }

    public void spawnEvilEye(int skillID, byte slv) {
        Field field;
        evilEye = Summon.getSummonBy(getCharacter(), skillID, slv);
        field = getCharacter().getField();
        evilEye.setFlyMob(true);
        evilEye.setMoveAbility(MoveAbility.FLY_AROUND_CHAR.getVal());
        field.spawnLife(evilEye);
    }

    public void removeEvilEye() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        tsm.removeStat(Beholder, true);
        getClient().write(new LP_TemporaryStatReset(tsm, false));
        getClient().write(new LP_SummonLeaveField(evilEye, LeaveType.ANIMATION));
        Field field = getCharacter().getField();
        field.removeLife(evilEye);
    }

}
