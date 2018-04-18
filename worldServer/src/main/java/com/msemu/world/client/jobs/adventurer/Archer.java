package com.msemu.world.client.jobs.adventurer;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.MobAttackInfo;
import com.msemu.world.client.character.skills.Option;
import com.msemu.world.client.character.skills.Skill;
import com.msemu.world.client.character.skills.SkillInfo;
import com.msemu.world.client.character.skills.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.Foothold;
import com.msemu.world.client.jobs.Job;
import com.msemu.world.client.life.AffectedArea;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.Summon;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.MobStat;
import com.msemu.world.enums.MoveAbility;
import com.msemu.world.enums.Stat;
import com.msemu.core.network.packets.out.WvsContext.StatChanged;
import com.msemu.core.network.packets.out.WvsContext.TemporaryStatSet;
import com.msemu.core.network.packets.out.WvsContext.TemporaryStateSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.msemu.world.client.character.skills.CharacterTemporaryStat.*;
import static com.msemu.world.enums.SkillStat.*;

/**
 * Created by Weber on 2018/4/14.
 */
public class Archer extends Job {
    public static final int MAPLE_RETURN = 1281;

    public static final int SOUL_ARROW_BOW = 3101004;
    public static final int SOUL_ARROW_XBOW = 3201004;
    public static final int ARROW_BOMB = 3101005;
    public static final int BOW_BOOSTER = 3101002;
    public static final int XBOW_BOOSTER = 3201002;
    public static final int QUIVER_CARTRIDGE = 3101009;
    public static final int QUIVER_CARTRIDGE_ATOM = 3100010; //3100010;
    public static final int FLAME_SURGE = 3111003;
    public static final int PHOENIX = 3111005;
    public static final int FREEZER = 3211005;
    public static final int RECKLESS_HUNT_BOW = 3111011;
    public static final int FOCUSED_FURY = 3110012;
    public static final int MORTAL_BLOW_BOW = 3110001;
    public static final int ARROW_PLATTER = 3111013;
    public static final int EVASION_BOOST = 3110007;
    public static final int SHARP_EYES_BOW = 3121002;
    public static final int SHARP_EYES_BOW_IED_H = 3120044;
    public static final int ILLUSION_STEP_BOW = 3121007;
    public static final int ENCHANTED_QUIVER = 3121016;
    public static final int BINDING_SHOT = 3121014;
    public static final int MAPLE_WARRIOR_BOW = 3121000;
    public static final int NET_TOSS = 3201008;
    public static final int PAIN_KILLER = 3211011;
    public static final int RECKLESS_HUNT_XBOW = 3211012;
    public static final int MORTAL_BLOW_XBOW = 3210001;
    public static final int AGGRESSIVE_RESISTANCE = 3210013;
    public static final int EVASION_BOOST_XBOW = 3210007;
    public static final int MAPLE_WARRIOR_XBOW = 3221000;
    public static final int ARROW_ILLUSION = 3221014;
    public static final int SHARP_EYES_XBOW = 3221002;
    public static final int SHARP_EYES_XBOW_IED_H = 3220044;
    public static final int ILLUSION_STEP_XBOW = 3221006;

    public static final int EPIC_ADVENTURE_XBOW = 3221053;
    public static final int EPIC_ADVENTURE_BOW = 3121053;
    public static final int CONCENTRATION = 3121054;
    public static final int BULLSEYE_SHOT = 3221054;

    //Final Attack
    public static final int FINAL_ATTACK_BOW = 3100001;
    public static final int ADVANCED_FINAL_ATTACK_BOW = 3120008;
    public static final int FINAL_ATTACK_XBOW = 3200001;


    private QuiverCartridge quiverCartridge;

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    private int[] buffs = new int[]{
            BOW_BOOSTER,
            XBOW_BOOSTER,
            SOUL_ARROW_BOW,
            SOUL_ARROW_XBOW,
            QUIVER_CARTRIDGE,
            PHOENIX,
            FREEZER,
            RECKLESS_HUNT_BOW,
            RECKLESS_HUNT_XBOW,
            SHARP_EYES_BOW,
            SHARP_EYES_XBOW,
            ILLUSION_STEP_BOW,
            ILLUSION_STEP_XBOW,
            ENCHANTED_QUIVER,
            MAPLE_WARRIOR_BOW,
            MAPLE_WARRIOR_XBOW,
            PAIN_KILLER,
            AGGRESSIVE_RESISTANCE,
            ARROW_ILLUSION,
            EPIC_ADVENTURE_BOW,
            EPIC_ADVENTURE_XBOW,
            CONCENTRATION,
            BULLSEYE_SHOT,
    };

    public Archer(Character character) {
        super(character);
        if (isHandlerOfJob(character.getJob())) {
            for (int id : addedSkills) {
                if (!character.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
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
            si = SkillData.getSkillInfoById(skill.getSkillId());
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
            case ARROW_BOMB:
                if (Rand.getChance(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case PHOENIX:
                if (Rand.getChance(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = 3;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case FLAME_SURGE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    AffectedArea aa = AffectedArea.getAffectedArea(attackInfo);
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    int x = mob.getX();
                    int y = mob.getY();
                    Foothold fh = mob.getCurFoodhold();
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
            case BINDING_SHOT:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o2.nOption = -si.getValue(x, slv);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(time, slv);
//                    mts.addStatOptions(MobStat.Re) // TODO hp recovery?
                    o1.nOption = si.getValue(s, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                }
                break;
            case NET_TOSS:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (mob.isBoss()) {
                            o1.nOption = si.getValue(x, slv);
                            o1.tOption = si.getValue(time, slv) / 2;
                        } else {
                            o1.nOption = si.getValue(y, slv);
                            o1.tOption = si.getValue(time, slv);
                        }
                        o1.rOption = skillID;
                        mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    }
                }
                break;
            case ARROW_ILLUSION:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(subTime, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case FREEZER:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(x, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
        }
    }

    private void handleAggresiveResistance(AttackInfo ai) {
        if (!getCharacter().hasSkill(AGGRESSIVE_RESISTANCE)) {
            return;
        }
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Skill skill = getCharacter().getSkill(AGGRESSIVE_RESISTANCE);
        SkillInfo si = SkillData.getSkillInfoById(AGGRESSIVE_RESISTANCE);
        byte slv = (byte) skill.getCurrentLevel();
        Option o = tsm.getOptByCTSAndSkill(DamAbsorbShield, AGGRESSIVE_RESISTANCE);
        Option o1 = new Option();
        long totalDamage = 0;
        for (MobAttackInfo mai : ai.mobAttackInfo) {
            for (int dmg : mai.damages) {
                totalDamage += dmg;
            }
        }
        if (o == null) {
            o = new Option();
            o.nOption = 0;
            o.rOption = AGGRESSIVE_RESISTANCE;
        }
        o.nOption = (int) Math.min((int) totalDamage * (si.getValue(y, slv) / 100D) + o.nOption,
                getCharacter().getStat(Stat.MAXHP) / (si.getValue(z, slv) / 100D));
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(DamAbsorbShield, o);
        tsm.sendSetStatPacket();
        handleAggressiveResistanceEffect();
    }

    private void handleAggressiveResistanceEffect() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o = new Option();
        Skill skill = getCharacter().getSkill(AGGRESSIVE_RESISTANCE);
        SkillInfo si = SkillData.getSkillInfoById(AGGRESSIVE_RESISTANCE);
        byte slv = (byte) skill.getCurrentLevel();
        o.nOption = 1;
        o.rOption = AGGRESSIVE_RESISTANCE;
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(PowerTransferGauge, o);
        getClient().write(new TemporaryStatSet(tsm));
    }

    private void handleMortalBlow() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Skill skill;
        SkillInfo si;
        byte slv;
        if (getCharacter().hasSkill(MORTAL_BLOW_BOW)) {
            skill = getCharacter().getSkill(MORTAL_BLOW_BOW);
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            Option o;
            if (!tsm.hasStat(BowMasterMortalBlow)) {
                o = new Option();
                o.rOption = MORTAL_BLOW_BOW;
            } else {
                o = tsm.getOption(BowMasterMortalBlow);
            }
            o.nOption = (o.nOption + 1) % (si.getValue(x, slv) + 1);
            getClient().write(new TemporaryStatSet(tsm));
        } else if (getCharacter().hasSkill(MORTAL_BLOW_XBOW)) {
            skill = getCharacter().getSkill(MORTAL_BLOW_BOW);
            //si = SkillData.getSkillInfoById(skill.getSkillId());
            //slv = (byte) skill.getCurrentLevel();
        }
    }

    private void handleFocusedFury() {
        if (!getCharacter().hasSkill(FOCUSED_FURY)) {
            return;
        }
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o = tsm.getOptByCTSAndSkill(AsrR, FOCUSED_FURY);
        if (o == null) {
            o = new Option();
            o.nOption = 0;
            o.rOption = FOCUSED_FURY;
        }
        Skill skill = getCharacter().getSkill(FOCUSED_FURY);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(FOCUSED_FURY);
        o.nOption = Math.min(o.nOption + si.getValue(x, slv), 100);
        o.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(AsrR, o);
        getClient().write(new TemporaryStatSet(tsm));
    }

    private void handleQuiverCartridge(TemporaryStatManager tsm, AttackInfo attackInfo, int slv) {
        Character chr = getCharacter();
        if (quiverCartridge == null) {
            return;
        }
        Skill skill = chr.hasSkill(ENCHANTED_QUIVER) ? chr.getSkill(ENCHANTED_QUIVER)
                : chr.getSkill(QUIVER_CARTRIDGE);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            MobTemporaryStat mts = mob.getTemporaryStat();
            int mobId = mai.mobId;
            switch (quiverCartridge.getType()) {
                case 1: // Blood
                    if (Rand.getChance(si.getValue(w, slv))) {
                        quiverCartridge.decrementAmount();
                        int maxHP = chr.getStat(Stat.MAXHP);
                        int addHP = (int) (maxHP * 0.03);
                        int curHP = chr.getStat(Stat.HP);
                        int newHP = curHP + addHP > maxHP ? maxHP : curHP + addHP;
                        chr.setStat(Stat.HP, (short) newHP);
                        Map<Stat, Object> stats = new HashMap<>();
                        stats.put(Stat.HP, newHP);
                        getClient().write(new StatChanged(stats));
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
//                        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 15, 15,
//                                num, 0, (int) System.currentTimeMillis(), 1, 0,
//                                new Position());
//                        chr.getField().broadcastPacket(Field.createForceAtom(false, 0, chr.getId(), type,
//                                true, mobId, QUIVER_CARTRIDGE_ATOM, forceAtomInfo, new Rect(), 0, 300,
//                                mob.getPosition(), 0, mob.getPosition()));
//                    }
//                    break;
            }
        }
        tsm.putCharacterStatValue(QuiverCatridge, quiverCartridge.getOption());
        getClient().write(new TemporaryStatSet(tsm));
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
            o.rOption = QUIVER_CARTRIDGE;
            o.xOption = type;
            return o;
        }

        public int getType() {
            return type;
        }
    }

    @Override
    public void handleSkillPacket(int skillID, byte slv, InPacket inPacket) {
        Character chr = getCharacter();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        if (isBuff(skillID)) {
            handleBuff(inPacket, skillID, slv);
        } else {
            Option o1 = new Option();
            switch (skillID) {
                case MAPLE_RETURN:
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
            if (getCharacter().hasSkill(EVASION_BOOST) || getCharacter().hasSkill(EVASION_BOOST_XBOW)) {
                Skill skill = getCharacter().getSkill(EVASION_BOOST);
                if (skill == null) {
                    skill = getCharacter().getSkill(EVASION_BOOST_XBOW);
                }
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
                Option o = new Option();
                o.nOption = 100;
                o.rOption = skill.getSkillId();
                o.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o);
                getClient().write(new TemporaryStateSet(tsm));
            }
        }

    }

    private void handleBuff(InPacket inPacket, int skillID, byte slv) {
        Character chr = getCharacter();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Summon summon;
        Field field;
        int curTime = (int) System.currentTimeMillis();
        switch (skillID) {
            case SOUL_ARROW_BOW:
            case SOUL_ARROW_XBOW:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulArrow, o1);
                o2.nOption = si.getValue(epad, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EPAD, o2);
                o1.nOption = 1; //si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o3);
                break;
            case BOW_BOOSTER:
            case XBOW_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case QUIVER_CARTRIDGE:
                if (quiverCartridge == null) {
                    quiverCartridge = new QuiverCartridge(chr);
                } else if (tsm.hasStat(QuiverCatridge)) {
                    quiverCartridge.incType();
                }
                o1 = quiverCartridge.getOption();
                tsm.putCharacterStatValue(QuiverCatridge, o1);
                break;
            case PHOENIX:
            case FREEZER:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                field = getCharacter().getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.FLY_AROUND_CHAR.getVal());
                field.spawnSummon(summon);

                break;
            case RECKLESS_HUNT_BOW:
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
            case PAIN_KILLER:
                o1.nOption = 100;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o1);
                break;
            case RECKLESS_HUNT_XBOW:
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
            case SHARP_EYES_BOW:
            case SHARP_EYES_XBOW:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);

                //mOption is for the hyper passive
                if (chr.hasSkill(SHARP_EYES_BOW_IED_H) || chr.hasSkill(SHARP_EYES_XBOW_IED_H)) {
                    o2.mOption = si.getValue(ignoreMobpdpR, slv);

                }
                tsm.putCharacterStatValue(SharpEyes, o2);
                break;
            case ILLUSION_STEP_BOW:
            case ILLUSION_STEP_XBOW:
                o1.nOption = si.getValue(dex, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEX, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVAR, o2);
                break;
            case MAPLE_WARRIOR_BOW:
            case MAPLE_WARRIOR_XBOW:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case ENCHANTED_QUIVER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AdvancedQuiver, o1);
                break;
            case ARROW_ILLUSION:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                summon.setMoveAbility(MoveAbility.STATIC.getVal());
                summon.setMaxHP(si.getValue(x, slv));
                field = getCharacter().getField();
                field.spawnSummon(summon);
                break;

            case EPIC_ADVENTURE_XBOW:
            case EPIC_ADVENTURE_BOW:
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
            case CONCENTRATION:
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
            case BULLSEYE_SHOT:
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
            if (chr.hasSkill(FINAL_ATTACK_BOW)) {
                fas = FINAL_ATTACK_BOW;
            }
            if (chr.hasSkill(FINAL_ATTACK_XBOW)) {
                fas = FINAL_ATTACK_XBOW;
            }
            if (chr.hasSkill(ADVANCED_FINAL_ATTACK_BOW)) {
                fas = ADVANCED_FINAL_ATTACK_BOW;
            }
            return fas;
        } else {
            return 0;
        }
    }

    private Skill getFinalAtkSkill(Character chr) {
        Skill skill = null;
        if (chr.hasSkill(FINAL_ATTACK_BOW)) {
            skill = chr.getSkill(FINAL_ATTACK_BOW);
        }
        if (chr.hasSkill(FINAL_ATTACK_XBOW)) {
            skill = chr.getSkill(FINAL_ATTACK_XBOW);
        }
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK_BOW)) {
            skill = chr.getSkill(ADVANCED_FINAL_ATTACK_BOW);
        }
        return skill;
    }

    private int getFinalAttackProc() {
        Skill skill = getFinalAtkSkill(getCharacter());
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) getCharacter().getSkill(skill.getSkillId()).getCurrentLevel();
        int proc = si.getValue(prop, slv);

        return proc;
    }

    public int getMaxNumberOfArrows(Character chr, int type) {
        int num = 0;
        Skill firstSkill = chr.getSkill(QUIVER_CARTRIDGE);
        Skill secondSkill = chr.getSkill(ENCHANTED_QUIVER);
        if (secondSkill != null && secondSkill.getCurrentLevel() > 0) {
            num = 20;

        } else if (firstSkill != null && firstSkill.getCurrentLevel() > 0) {
            num = 10;
        }
        return type == 3 ? num * 2 : num; // Magic Arrow has 2x as many arrows
    }
}
