package com.msemu.world.client.character.jobs.adventurer;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.packets.out.wvscontext.LP_StatChanged;
import com.msemu.core.network.packets.out.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.*;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.skill.Option;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.skill.TemporaryStatManager;
import com.msemu.world.client.field.AffectedArea;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.Summon;
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.MoveAbility;
import com.msemu.world.enums.Stat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.skill.CharacterTemporaryStat.*;

/**
 * Created by Weber on 2018/4/14.
 */
public class Archer extends JobHandler {
    public static final int 回歸楓之谷 = 1281;
    public static final int 無形之箭_弓 = 3101004;
    public static final int 無形之箭_弩 = 3201004;
    public static final int 炸彈箭 = 3101005;
    public static final int 快速之弓 = 3101002;
    public static final int 快速之弩 = 3201002;
    public static final int 魔幻箭筒 = 3101009;
    public static final int 魔幻箭筒_ATOM = 3100010; //3100010;
    public static final int 火焰衝擊 = 3111003;
    public static final int 召喚鳳凰 = 3111005;
    public static final int 召喚銀隼 = 3211005;
    public static final int 終極射擊_箭 = 3111011;
    public static final int 集中專注 = 3110012;
    public static final int 致命箭_弓 = 3110001;
    public static final int 箭座 = 3111013;
    public static final int 躲避_弓 = 3110007;
    public static final int 會心之眼_弓 = 3121002;
    public static final int 會心之眼_無視防禦_弓 = 3120044;
    public static final int 幻影踏步 = 3121007;
    public static final int 無限箭筒 = 3121016;
    public static final int 追傷之箭 = 3121014;
    public static final int 楓葉祝福_弓 = 3121000;
    public static final int 撒網 = 3201008;
    public static final int 痛苦殺手 = 3211011;
    public static final int 終極射擊_弩 = 3211012;
    public static final int 致命箭_弩 = 3210001;
    public static final int 反向傷害 = 3210013;
    public static final int 躲避_弩 = 3210007;
    public static final int 楓葉祝福_弩 = 3221000;
    public static final int 幻像箭影 = 3221014;
    public static final int 會心之眼_弩 = 3221002;
    public static final int 會心之眼_無視防禦_弩 = 3220044;
    public static final int 四連殺爆發_弩 = 3221006;

    public static final int 傳說冒險_弓 = 3221053;
    public static final int 傳說冒險_弩 = 3121053;
    public static final int 戰鬥準備 = 3121054;
    public static final int 專注弱點 = 3221054;

    //Final Attack
    public static final int 終極之弓 = 3100001;
    public static final int 進階終極攻擊_弓 = 3120008;
    public static final int 終極之弩 = 3200001;


    private QuiverCartridge quiverCartridge;

    private int[] addedSkills = new int[]{
            回歸楓之谷,
    };

    private int[] buffs = new int[]{
            快速之弓,
            快速之弩,
            無形之箭_弓,
            無形之箭_弩,
            魔幻箭筒,
            召喚鳳凰,
            召喚銀隼,
            終極射擊_箭,
            終極射擊_弩,
            會心之眼_弓,
            會心之眼_弩,
            幻影踏步,
            四連殺爆發_弩,
            無限箭筒,
            楓葉祝福_弓,
            楓葉祝福_弩,
            痛苦殺手,
            反向傷害,
            幻像箭影,
            傳說冒險_弩,
            傳說冒險_弓,
            戰鬥準備,
            專注弱點,
    };

    public Archer(Character character) {
        super(character);
        if (isHandlerOfJob(character.getJob())) {
            for (int id : addedSkills) {
                if (!character.hasSkill(id)) {
                    Skill skill = SkillData.getInstance().getSkillById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    character.addSkill(skill);
                }
            }
        }
    }

    @Override
    public void handleAttack(AttackInfo attackInfo) {
        Character chr = getCharacter();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
            slv = skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }
        if (hasHitMobs) {
            handleQuiverCartridge(chr.getTemporaryStatManager(), attackInfo, slv);
            handleFocusedFury();
            handleMortalBlow();
            handleAggresiveResistance(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case 炸彈箭:
                if (Rand.getChance(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 召喚鳳凰:
                if (Rand.getChance(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = 3;
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 火焰衝擊:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    AffectedArea aa = AffectedArea.getAffectedArea(attackInfo);
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    int x = mob.getPosition().getX();
                    int y = mob.getPosition().getY();
                    aa.setPosition(new Position(x, y));
                    Rect rect = si.getRects().get(0);
//                    if(rect.getLeft() > fh.getX1()) {
//                        rect.setLeft(fh.getX1());
//                    } else if(rect.getRight() > fh.getX2()) {
//                        rect.setRight(fh.getX2());
//                    }
                    aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                    chr.getField().spawnAffectedArea(aa);
                }
                break;
            case 追傷之箭:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o2.nOption = -si.getValue(x, slv);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(time, slv);
//                    mts.addStatOptions(MobBuffStat.Re) // TODO hp recovery?
                    o1.nOption = si.getValue(s, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Speed, o1);
                }
                break;
            case 撒網:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (mob.isBoss()) {
                            o1.nOption = si.getValue(x, slv);
                            o1.tOption = si.getValue(time, slv) / 2;
                        } else {
                            o1.nOption = si.getValue(y, slv);
                            o1.tOption = si.getValue(time, slv);
                        }
                        o1.rOption = skillID;
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Speed, o1);
                    }
                }
                break;
            case 幻像箭影:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(subTime, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 召喚銀隼:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(x, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Freeze, o1);
                }
                break;
        }
    }

    private void handleAggresiveResistance(AttackInfo ai) {
        if (!getCharacter().hasSkill(反向傷害)) {
            return;
        }
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Skill skill = getCharacter().getSkill(反向傷害);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(反向傷害);
        byte slv = (byte) skill.getCurrentLevel();
        Option o = tsm.getOptByCTSAndSkill(DamAbsorbShield, 反向傷害);
        Option o1 = new Option();
        long totalDamage = 0;
        for (MobAttackInfo mai : ai.mobAttackInfo) {
            for (long dmg : mai.getDamages()) {
                totalDamage += dmg;
            }
        }
        if (o == null) {
            o = new Option();
            o.nOption = 0;
            o.rOption = 反向傷害;
        }
        o.nOption = (int) Math.min((int) totalDamage * (si.getValue(y, slv) / 100D) + o.nOption,
                getCharacter().getStat(Stat.MAX_HP) / (si.getValue(z, slv) / 100D));
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(DamAbsorbShield, o);
        tsm.sendSetStatPacket();
        handleAggressiveResistanceEffect();
    }

    private void handleAggressiveResistanceEffect() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o = new Option();
        Skill skill = getCharacter().getSkill(反向傷害);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(反向傷害);
        byte slv = (byte) skill.getCurrentLevel();
        o.nOption = 1;
        o.rOption = 反向傷害;
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(PowerTransferGauge, o);
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private void handleMortalBlow() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Skill skill;
        SkillInfo si;
        byte slv;
        if (getCharacter().hasSkill(致命箭_弓)) {
            skill = getCharacter().getSkill(致命箭_弓);
            si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            Option o;
            if (!tsm.hasStat(BowMasterMortalBlow)) {
                o = new Option();
                o.rOption = 致命箭_弓;
            } else {
                o = tsm.getOption(BowMasterMortalBlow);
            }
            o.nOption = (o.nOption + 1) % (si.getValue(x, slv) + 1);
            getClient().write(new LP_TemporaryStatSet(tsm));
        } else if (getCharacter().hasSkill(致命箭_弩)) {
            skill = getCharacter().getSkill(致命箭_弓);
            //si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
            //slv = (byte) skill.getCurrentLevel();
        }
    }

    private void handleFocusedFury() {
        if (!getCharacter().hasSkill(集中專注)) {
            return;
        }
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o = tsm.getOptByCTSAndSkill(AsrR, 集中專注);
        if (o == null) {
            o = new Option();
            o.nOption = 0;
            o.rOption = 集中專注;
        }
        Skill skill = getCharacter().getSkill(集中專注);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getInstance().getSkillInfoById(集中專注);
        o.nOption = Math.min(o.nOption + si.getValue(x, slv), 100);
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(AsrR, o);
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private void handleQuiverCartridge(TemporaryStatManager tsm, AttackInfo attackInfo, int slv) {
        Character chr = getCharacter();
        if (quiverCartridge == null) {
            return;
        }
        Skill skill = chr.hasSkill(無限箭筒) ? chr.getSkill(無限箭筒)
                : chr.getSkill(魔幻箭筒);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
            MobTemporaryStat mts = mob.getTemporaryStat();
            int mobId = mai.getObjectID();
            switch (quiverCartridge.getType()) {
                case 1: // Blood
                    if (Rand.getChance(si.getValue(w, slv))) {
                        quiverCartridge.decrementAmount();
                        int maxHP = chr.getStat(Stat.MAX_HP);
                        int addHP = (int) (maxHP * 0.03);
                        int curHP = chr.getStat(Stat.HP);
                        int newHP = curHP + addHP > maxHP ? maxHP : curHP + addHP;
                        chr.setStat(Stat.HP, (short) newHP);
                        Map<Stat, Object> stats = new HashMap<>();
                        stats.put(Stat.HP, newHP);
                        getClient().write(new LP_StatChanged(stats));
                    }
                    break;
                case 2: // Poison
                    mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    quiverCartridge.decrementAmount();
                    break;
                case 3: // Magic
//                    int num;
//                    if (Rand.nextBoolean()) {
//                        num = 50;
//                    } else {
//                        num = 130;
//                    }
//                    if (Rand.getChance(si.getValue(u, slv))) {
//                        quiverCartridge.decrementAmount();
//                        int inc = ForceAtomEnum.BM_ARROW.getInc();
//                        int type = ForceAtomEnum.BM_ARROW.getForceAtomType();
//                        AbstractForceAtom forceAtomInfo = new AbstractForceAtom(1, inc, 15, 15,
//                                num, 0, (int) System.currentTimeMillis(), 1, 0,
//                                new Position());
//                        chr.getField().broadcastPacket(field.createForceAtom(false, 0, chr.getItemId(), type,
//                                true, mobId, 魔幻箭筒_ATOM, forceAtomInfo, new Rect(), 0, 300,
//                                mob.getPosition(), 0, mob.getPosition()));
//                    }
//                    break;
            }
        }
        tsm.putCharacterStatValue(QuiverCatridge, quiverCartridge.getOption());
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    @Override
    public void handleSkillUse(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final Skill skill = chr.getSkill(skillID);
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = skill != null ? getSkillInfo(skillID) : null;
        if (si == null) {
            return;
        }
        if (isBuff(skillID)) {
            handleBuff(skillUseInfo);
        } else {
            Option o1 = new Option();
            switch (skillID) {
                case 回歸楓之谷:
                    o1.nValue = si.getValue(x, slv);
                    Field toField = getClient().getChannelInstance().getField(o1.nValue);
                    chr.warp(toField);
                    break;

            }
        }
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {
        if (hitInfo.getHPDamage() == 0 && hitInfo.getMPDamage() == 0) {
            // Dodged
            if (getCharacter().hasSkill(躲避_弓) || getCharacter().hasSkill(躲避_弩)) {
                Skill skill = getCharacter().getSkill(躲避_弓);
                if (skill == null) {
                    skill = getCharacter().getSkill(躲避_弩);
                }
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
                TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
                Option o = new Option();
                o.nOption = 100;
                o.rOption = skill.getSkillId();
                o.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o);
                getClient().write(new LP_TemporaryStatSet(tsm));
            }
        }

    }

    public void handleBuff(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = getSkillInfo(skillID);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Summon summon;
        Field field;
        int curTime = (int) System.currentTimeMillis();
        switch (skillID) {
            case 無形之箭_弓:
            case 無形之箭_弩:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulArrow, o1);
                o2.nOption = si.getValue(epad, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EPAD, o2);
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o3);
                break;
            case 快速之弓:
            case 快速之弩:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case 魔幻箭筒:
                if (quiverCartridge == null) {
                    quiverCartridge = new QuiverCartridge(chr);
                } else if (tsm.hasStat(QuiverCatridge)) {
                    quiverCartridge.incType();
                }
                o1 = quiverCartridge.getOption();
                tsm.putCharacterStatValue(QuiverCatridge, o1);
                break;
            case 召喚鳳凰:
            case 召喚銀隼:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                field = getCharacter().getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.FLY_AROUND_CHAR.getVal());
                field.spawnSummon(summon);

                break;
            case 終極射擊_箭:
                o1.nValue = -si.getValue(x, slv);
                o1.nReason = skillID;
                tsm.putCharacterStatValue(IndiePADR, o1);
                tsm.putCharacterStatValue(IndieMADR, o1);
                o2.nValue = si.getValue(indieDamR, slv);
                o2.nReason = skillID;
                tsm.putCharacterStatValue(IndieDamR, o2);
                o3.nOption = si.getValue(padX, slv);
                o3.rOption = skillID;
                tsm.putCharacterStatValue(PAD, o3);
                break;
            case 痛苦殺手:
                o1.nOption = 100;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o1);
                break;
            case 終極射擊_弩:
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(EVAR, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IncCriticalDamMax, o2);
                o3.nOption = si.getValue(z, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IncCriticalDamMin, o3);
                break;
            case 會心之眼_弓:
            case 會心之眼_弩:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);

                //mOption is for the hyper passive
                if (chr.hasSkill(會心之眼_無視防禦_弓) || chr.hasSkill(會心之眼_無視防禦_弩)) {
                    o2.mOption = si.getValue(ignoreMobpdpR, slv);

                }
                tsm.putCharacterStatValue(SharpEyes, o2);
                break;
            case 幻影踏步:
            case 四連殺爆發_弩:
                o1.nOption = si.getValue(dex, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEX, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVAR, o2);
                break;
            case 楓葉祝福_弓:
            case 楓葉祝福_弩:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case 無限箭筒:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AdvancedQuiver, o1);
                break;
            case 幻像箭影:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                summon.setMoveAbility(MoveAbility.STATIC.getVal());
                summon.setMaxHP(si.getValue(x, slv));
                field = getCharacter().getField();
                field.spawnSummon(summon);
                break;

            case 傳說冒險_弓:
            case 傳說冒險_弩:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
            case 戰鬥準備:
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o2);
                o3.nOption = si.getValue(y, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Preparation, o3); //preparation = BD%
                break;
            case 專注弱點:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BullsEye, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                o3.tStart = (int) System.currentTimeMillis();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o3);
                o4.nOption = si.getValue(y, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SharpEyes, o4);   //Max Crit Dmg%
                o5.nReason = skillID;
                o5.nValue = si.getValue(x, slv);
                o5.tStart = (int) System.currentTimeMillis();
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o5);
                break;
        }
        tsm.sendSetStatPacket();
    }

    public boolean isBuff(int skillID) {
        return Arrays.stream(buffs).anyMatch(b -> b == skillID);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        MapleJob job = MapleJob.getById(id);
        switch (job) {
            case 弓箭手:
            case 獵人:
            case 弩弓手:
            case 遊俠:
            case 狙擊手:
            case 箭神:
            case 神射手:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getFinalAttackSkill() {
        Character chr = getCharacter();
        if (Rand.getChance(getFinalAttackProc())) {
            int fas = 0;
            if (chr.hasSkill(終極之弓)) {
                fas = 終極之弓;
            }
            if (chr.hasSkill(終極之弩)) {
                fas = 終極之弩;
            }
            if (chr.hasSkill(進階終極攻擊_弓)) {
                fas = 進階終極攻擊_弓;
            }
            return fas;
        } else {
            return 0;
        }
    }

    private Skill getFinalAtkSkill(Character chr) {
        Skill skill = null;
        if (chr.hasSkill(終極之弓)) {
            skill = chr.getSkill(終極之弓);
        }
        if (chr.hasSkill(終極之弩)) {
            skill = chr.getSkill(終極之弩);
        }
        if (chr.hasSkill(進階終極攻擊_弓)) {
            skill = chr.getSkill(進階終極攻擊_弓);
        }
        return skill;
    }

    private int getFinalAttackProc() {
        Skill skill = getFinalAtkSkill(getCharacter());
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
        byte slv = (byte) getCharacter().getSkill(skill.getSkillId()).getCurrentLevel();
        int proc = si.getValue(prop, slv);

        return proc;
    }

    public int getMaxNumberOfArrows(Character chr, int type) {
        int num = 0;
        Skill firstSkill = chr.getSkill(魔幻箭筒);
        Skill secondSkill = chr.getSkill(無限箭筒);
        if (secondSkill != null && secondSkill.getCurrentLevel() > 0) {
            num = 20;

        } else if (firstSkill != null && firstSkill.getCurrentLevel() > 0) {
            num = 10;
        }
        return type == 3 ? num * 2 : num; // Magic Arrow has 2x as many arrows
    }

    public enum QCType {
        BLOOD(1),
        POISON(2),
        MAGIC(3),;
        private byte val;

        QCType(int val) {
            this.val = (byte) val;
        }

        public byte getVal() {
            return val;
        }
    }

    public class QuiverCartridge {

        private int blood; // 1
        private int poison; // 2
        private int magic; // 3
        private int type;
        private Character chr;

        public QuiverCartridge(Character chr) {
            blood = getMaxNumberOfArrows(chr, QCType.BLOOD.getVal());
            poison = getMaxNumberOfArrows(chr, QCType.POISON.getVal());
            magic = getMaxNumberOfArrows(chr, QCType.MAGIC.getVal());
            type = 1;
            this.chr = chr;
        }

        public void decrementAmount() {
            if (chr.getTemporaryStatManager().hasStat(AdvancedQuiver)) {
                return;
            }
            switch (type) {
                case 1:
                    blood--;
                    if (blood == 0) {
                        blood = getMaxNumberOfArrows(chr, QCType.BLOOD.getVal());
                        incType();
                    }
                    break;
                case 2:
                    poison--;
                    if (poison == 0) {
                        poison = getMaxNumberOfArrows(chr, QCType.POISON.getVal());
                        incType();
                    }
                    break;
                case 3:
                    magic--;
                    if (magic == 0) {
                        magic = getMaxNumberOfArrows(chr, QCType.MAGIC.getVal());
                        incType();
                    }
                    break;
            }
        }

        public void incType() {
            type = (type % 3) + 1;
        }

        public int getTotal() {
            return blood * 10000 + poison * 100 + magic;
        }

        public Option getOption() {
            Option o = new Option();
            o.nOption = getTotal();
            o.rOption = 魔幻箭筒;
            o.xOption = type;
            return o;
        }

        public int getType() {
            return type;
        }
    }
}
