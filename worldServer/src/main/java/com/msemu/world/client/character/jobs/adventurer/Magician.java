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

package com.msemu.world.client.character.jobs.adventurer;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.*;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.character.stats.TemporaryStatManager;
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
import java.util.List;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.stats.CharacterTemporaryStat.*;

/**
 * Created by Weber on 2018/5/18.
 */
public class Magician extends JobHandler {
    public static final int MAPLE_RETURN = 1281;

    public static final int MAGIC_GUARD = 2001002;
    public static final int TELEPORT = 2001009;
    public static final int MP_EATER_FP = 2100000;
    public static final int POISON_BREATH = 2101005;
    public static final int MAGIC_BOOSTER_FP = 2101008;
    public static final int MEDITATION_FP = 2101001;
    public static final int IGNITE = 2101010;
    public static final int IGNITE_AA = 2100010;
    public static final int BURNING_MAGIC = 2110000;
    public static final int POISON_MIST = 2111003;
    public static final int TELEPORT_MASTERY_FP = 2111007;
    public static final int ELEMENTAL_DECREASE_FP = 2111008;
    public static final int VIRAL_SLIME = 2111010;
    public static final int PARALYZE = 2121006;
    public static final int MIST_ERUPTION = 2121003;
    public static final int FLAME_HAZE = 2121011;
    public static final int INFINITY_FP = 2121004;
    public static final int IFRIT = 2121005;
    public static final int MAPLE_WARRIOR_FP = 2121000;
    public static final int CHILLING_STEP = 2201009;
    public static final int COLD_BEAM = 2201008;
    public static final int FREEZING_CRUSH = 2200011;   //TODO Set stacks on mobs, gain buff from those stacks
    public static final int FROST_CLUTCH = 2220015;
    public static final int ELEMENTAL_DRAIN = 2100009;
    public static final int FERVENT_DRAIN = 2120014;
    public static final int MAGIC_BOOSTER_IL = 2201010;
    public static final int MEDITATION_IL = 2201001;
    public static final int ICE_STRIKE = 2211002;
    public static final int GLACIER_CHAIN = 2211010;
    public static final int THUNDER_STORM = 2211011;
    public static final int TELEPORT_MASTERY_IL = 2211007;
    public static final int ELEMENTAL_DECREASE_IL = 2211008;
    public static final int CHAIN_LIGHTNING = 2221006;
    public static final int FREEZING_BREATH = 2221011;
    public static final int INFINITY_IL = 2221004;
    public static final int ELQUINES = 2221005;
    public static final int HEAL = 2301002;
    public static final int MAGIC_BOOSTER_BISH = 2301008;
    public static final int BLESS = 2301004;
    public static final int HOLY_MAGIC_SHELL = 2311009;
    public static final int TELEPORT_MASTERY_BISH = 2311007;
    public static final int HOLY_FOUNTAIN = 2311011;
    public static final int DIVINE_PROTECTION = 2311012;
    public static final int MYSTIC_DOOR = 2311002;
    public static final int HOLY_SYMBOL = 2311003;
    public static final int ADV_BLESSING = 2321005;
    public static final int BAHAMUT = 2321003;
    public static final int INFINITY_BISH = 2321004;
    public static final int MAPLE_WARRIOR_BISH = 2321000;
    public static final int MAPLE_WARRIOR_IL = 2221000;
    public static final int ARCANE_AIM_FP = 2120010;
    public static final int ARCANE_AIM_IL = 2220010;
    public static final int ARCANE_AIM_BISH = 2320011;

    //Hypers
    public static final int EPIC_ADVENTURE_FP = 2121053;
    public static final int EPIC_ADVENTURE_IL = 2221053;
    public static final int EPIC_ADVENTURE_BISH = 2321053;
    public static final int ABSOLUTE_ZERO_AURA = 2121054;
    public static final int INFERNO_AURA = 2221054;
    public static final int RIGHTEOUSLY_INDIGNANT = 2321054;
    public static final int HEAVENS_DOOR = 2321055;
    public static final int MEGIDDO_FLAME = 2121052;
    public static final int MEGIDDO_FLAME_ATOM = 2121055;
    private final int[] buffs = new int[]{
            MAGIC_GUARD,
            IGNITE,
            MAGIC_BOOSTER_FP,
            MEDITATION_FP,
            TELEPORT_MASTERY_FP,
            ELEMENTAL_DECREASE_FP,
            INFINITY_FP,
            IFRIT,
            MAPLE_WARRIOR_FP,
            MEDITATION_FP,
            CHILLING_STEP,
            MAGIC_BOOSTER_IL,
            MEDITATION_IL,
            THUNDER_STORM,
            TELEPORT_MASTERY_IL,
            ELEMENTAL_DECREASE_IL,
            INFINITY_IL,
            ELQUINES,
            MAPLE_WARRIOR_IL,
            VIRAL_SLIME,
            MAGIC_BOOSTER_BISH,
            BLESS,
            HOLY_MAGIC_SHELL,
            TELEPORT_MASTERY_BISH,
            DIVINE_PROTECTION,
            MYSTIC_DOOR,
            HOLY_SYMBOL,
            ADV_BLESSING,
            MAPLE_WARRIOR_BISH,
            INFINITY_BISH,
            BAHAMUT,

            EPIC_ADVENTURE_FP,
            EPIC_ADVENTURE_IL,
            EPIC_ADVENTURE_BISH,
            ABSOLUTE_ZERO_AURA,
            INFERNO_AURA,
            RIGHTEOUSLY_INDIGNANT,
    };
    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    public Magician(Character character) {
        super(character);
        if (isHandlerOfJob(getCharacter().getJob())) {
            for (int id : addedSkills) {
                if (!getCharacter().hasSkill(id)) {
                    Skill skill = SkillData.getInstance().getSkillById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    getCharacter().addSkill(skill);
                }
            }
        }
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

        if (hasHitMobs) {                                   //Common
            handleArcaneAim();
            handleMegiddoFlameReCreation(skillID, (byte) slv, attackInfo);
        }

        if (chr.getJob() >= MapleJob.巫師_火毒.getId() && chr.getJob() <= MapleJob.大魔導士_火毒.getId()) {
            if (hasHitMobs) {
                //Ignite
                handleIgnite(attackInfo);

                //Elemental Drain
                handleElementalDrain();
            }
        }

        if (chr.getJob() >= MapleJob.巫師_冰雷.getId() && chr.getJob() <= MapleJob.大魔導士_冰雷.getId()) {
            if (hasHitMobs) {
                //Freezing Crush / Frozen Clutch
                handleFreezingCrush(attackInfo, (byte) slv);
            }
        }

        if (chr.getJob() >= MapleJob.僧侶.getId() && chr.getJob() <= MapleJob.主教.getId()) {
            if (hasHitMobs) {

            }
        }

        if (normalAttack)
            return true;

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case POISON_BREATH:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                }
                break;
            case POISON_MIST:
                AffectedArea aa = AffectedArea.getAffectedArea(attackInfo);
                aa.setMobOrigin((byte) 0);
                aa.setCharID(chr.getId());
                int x = attackInfo.forcedX;
                int y = attackInfo.forcedY;
                aa.setPosition(new Position(x, y));
                aa.setRect(aa.getPosition().getRectAround(si.getRect(slv)));
                aa.setDelay((short) 9);
                chr.getField().spawnAffectedArea(aa);
                break;
            case TELEPORT_MASTERY_FP:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptions(MobBuffStat.Stun, o1);
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                }
                break;
            case FLAME_HAZE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptions(MobBuffStat.Invincible, o1);
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptions(MobBuffStat.Speed, o1);
                        mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    }
                    AffectedArea aa2 = AffectedArea.getAffectedArea(attackInfo);
                    aa2.setMobOrigin((byte) 0);
                    aa2.setCharID(chr.getId());
                    aa2.setPosition(mob.getPosition());
                    aa2.setRect(aa2.getPosition().getRectAround(si.getRect()));
                    chr.getField().spawnAffectedArea(aa2);
                }
                break;
            case IFRIT:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                }
                break;
            case PARALYZE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr.getId(), skill, 1);
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                }
                break;
            case COLD_BEAM:
            case ICE_STRIKE:
            case GLACIER_CHAIN:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 5;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Freeze, o1);
                }
                break;
            case TELEPORT_MASTERY_IL:
            case CHAIN_LIGHTNING:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Rand.getChance(si.getValue(prop, slv))) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case MIST_ERUPTION:
//                for (int objectId : attackInfo.mists) {
//                    Field field = chr.getField();
//
//                    field.removeLife(objectId);
//                }
                break;
            case BAHAMUT:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 25;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(subTime, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.AddDamParty, o1);
                }
                break;
            case HEAVENS_DOOR:
                o1.nOption = 1;
                o1.rOption = HEAVENS_DOOR;
                o1.tOption = 0;
                tsm.putCharacterStatValue(HeavensDoor, o1);
                getClient().write(new LP_TemporaryStatSet(tsm));
                break;
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
            switch (skillID) {
                case MAPLE_RETURN:
                    o1.nValue = si.getValue(x, slv);
                    Field toField = getClient().getChannelInstance().getField(o1.nValue);
                    chr.warp(toField);
                    break;
                case FREEZING_BREATH:
                    break;
                case MEGIDDO_FLAME:
                    handleMegiddoFlame();
                    break;
                case HOLY_FOUNTAIN: //User_Create_Holidom_Request  needs to be created
                    AffectedArea aa = AffectedArea.getPassiveAA(skillID, slv);
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    aa.setPosition(chr.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(si.getRect()));
                    aa.setDelay((short) 4);
                    chr.getField().spawnAffectedArea(aa);
                    break;
                case TELEPORT:
                    handleChillingStep();
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return MapleJob.is法師(id);
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }

    @Override
    public boolean isBuff(int skillID) {
        return Arrays.stream(buffs).anyMatch(b -> b == skillID);
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {
        Character chr = getCharacter();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(MagicGuard)) {
            Skill skill = chr.getSkill(MAGIC_GUARD);
            SkillInfo si = SkillData.getInstance().getSkillInfoById(MAGIC_GUARD);
            int dmgPercent = si.getValue(x, skill.getCurrentLevel());
            int dmg = hitInfo.getHPDamage();
            int mpDmg = (int) (dmg * (dmgPercent / 100D));
            mpDmg = chr.getStat(Stat.MP) - mpDmg < 0 ? chr.getStat(Stat.MP) : mpDmg;
            hitInfo.setHPDamage(dmg - mpDmg);
            hitInfo.setMPDamage(mpDmg);
        }
    }

    private void handleChillingStep() {
        Character chr = getCharacter();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(ChillingStep)) {
            for (int i = 0; i < 168; i += 56) {
                SkillInfo chillingStepInfo = SkillData.getInstance().getSkillInfoById(CHILLING_STEP);
                Skill skill = chr.getSkill(CHILLING_STEP);
                int slv = skill.getCurrentLevel();
                AffectedArea aa = AffectedArea.getPassiveAA(CHILLING_STEP, (byte) slv);
                aa.setMobOrigin((byte) 0);
                aa.setCharID(chr.getId());
                int x = chr.isLeft() ? chr.getPosition().getX() - i : chr.getPosition().getX() + i;
                int y = chr.getPosition().getY();
                aa.setPosition(new Position(x, y));
                aa.setRect(aa.getPosition().getRectAround(chillingStepInfo.getRect()));
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa);
            }
        }
    }

    private void handleElementalDrain() {
        Character chr = getCharacter();
        Skill skill = chr.getSkill(getElementalDrainSkill());
        SkillInfo edi = SkillData.getInstance().getSkillInfoById(getElementalDrainSkill());
        byte slv = (byte) skill.getCurrentLevel();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Option o1 = new Option();
        int amount = 1;
        if (tsm.hasStat(DotBasedBuff)) {
            amount = tsm.getOption(DotBasedBuff).nOption;
            if (amount < 5) {
                amount++;
            }
        }
        o.nOption = amount;
        o.rOption = ELEMENTAL_DRAIN;
        o.tOption = 0; // No Time Variable
        tsm.putCharacterStatValue(DotBasedBuff, o);
        o1.nOption = (amount * 5);
        o1.rOption = ELEMENTAL_DRAIN;
        o1.tOption = 0; // No Time Variable
        tsm.putCharacterStatValue(DamR, o1);
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private void handleMegiddoFlame() {
        Character chr = getCharacter();
        Field field = chr.getField();
        SkillInfo si = SkillData.getInstance().getSkillInfoById(MEGIDDO_FLAME);
        Rect rect = chr.getPosition().getRectAround(si.getRect());
        List<Mob> mobs = field.getMobInRect(rect);
        for (Mob mob : mobs) {
            int mobID = mob.getRefImgMobID(); //
            int mobID2 = mob.getObjectId();
            int mobID3 = mob.getTemplateId();
            int x = mob.getPosition().getX();
            int y = mob.getPosition().getY();
//            int inc = ForceAtomEnum.DA_ORB.getInc();
//            int type = ForceAtomEnum.DA_ORB.getForceAtomType();
//            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 20, 40,
//                    0, 500, (int) System.currentTimeMillis(), 1, 0,
//                    new Position(0, 0));
//            chr.getField().broadcast(CField.createForceAtom(false, 0, chr.getId(), type,
//                    true, mobID, MEGIDDO_FLAME_ATOM, forceAtomInfo, new Rect(), 0, 300,
//                    mob.getPosition(), MEGIDDO_FLAME_ATOM, mob.getPosition()));

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
        Option o6 = new Option();
        Option o7 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case MAGIC_GUARD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(MagicGuard, o1);
                break;
            case MAGIC_BOOSTER_FP:
            case MAGIC_BOOSTER_IL:
            case MAGIC_BOOSTER_BISH:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
//                o1.nValue = si.getValue(x, slv);
//                o1.nReason = skillID;
//                o1.tStart= (int) System.currentTimeMillis();
//                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case MEDITATION_FP:
            case MEDITATION_IL:
                o1.nValue = si.getValue(indieMad, slv);
                o1.nReason = skillID;
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                break;
            case IGNITE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(WizardIgnite, o1);
                break;
            case ELEMENTAL_DECREASE_FP:
            case ELEMENTAL_DECREASE_IL:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ElementalReset, o1);
                break;
            case TELEPORT_MASTERY_FP:
            case TELEPORT_MASTERY_IL:
            case TELEPORT_MASTERY_BISH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(TeleportMasteryOn, o1);
                break;
            case INFINITY_FP:
            case INFINITY_IL:
            case INFINITY_BISH:
                o1.nOption = si.getValue(damage, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Infinity, o1);
                break;
            case VIRAL_SLIME:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.SLOW_FORWARD.getVal());
                field.spawnLife(summon);
                break;
            case BLESS:

                break;
            case ADV_BLESSING:
                break;
            case HOLY_SYMBOL:
                //Holy symbol
                break;
            case HOLY_MAGIC_SHELL:
                //HolyMagicShell
                break;
            case IFRIT:
            case ELQUINES:
            case BAHAMUT:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = getCharacter().getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.FOLLOW.getVal());
                field.spawnLife(summon);
                break;
            case MAPLE_WARRIOR_FP:
            case MAPLE_WARRIOR_IL:
            case MAPLE_WARRIOR_BISH:
                o1.nValue = si.getValue(x, slv);
                o1.nReason = skillID;
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case THUNDER_STORM:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                field.spawnLife(summon);
                break;
            case CHILLING_STEP:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(ChillingStep, o1);
                break;

            case EPIC_ADVENTURE_FP:
            case EPIC_ADVENTURE_IL:
            case EPIC_ADVENTURE_BISH:
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
            case ABSOLUTE_ZERO_AURA:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(FireAura, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = 0;
                tsm.putCharacterStatValue(Stance, o2);
                o3.nOption = si.getValue(y, slv);
                o3.rOption = skillID;
                o3.tOption = 0;
                tsm.putCharacterStatValue(DamAbsorbShield, o3);
                o4.nOption = si.getValue(v, slv);
                o4.rOption = skillID;
                o4.tOption = 0;
                tsm.putCharacterStatValue(AsrR, o4);
                tsm.putCharacterStatValue(TerR, o4);
                break;
            case INFERNO_AURA:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(IceAura, o1);
                break;
            case RIGHTEOUSLY_INDIGNANT: //TODO recovey amount (Heal?) hp %
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(VengeanceOfAngel, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMad, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = 0;
                tsm.putCharacterStatValue(IndieMAD, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indiePMdR, slv);
                o3.tStart = (int) System.currentTimeMillis();
                o3.tTerm = 0;
                tsm.putCharacterStatValue(IndiePMdR, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieMaxDamageOver, slv);
                o4.tStart = (int) System.currentTimeMillis();
                o4.tTerm = 0;
                tsm.putCharacterStatValue(IndieMaxDamageOver, o4);
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieBooster, slv);
                o5.tStart = (int) System.currentTimeMillis();
                o5.tTerm = 0;
                tsm.putCharacterStatValue(IndieBooster, o5);
                o6.nOption = si.getValue(ignoreMobpdpR, slv);
                o6.rOption = skillID;
                o6.tOption = 0;
                tsm.putCharacterStatValue(IgnoreMobpdpR, o6);
                o7.nOption = si.getValue(w, slv);
                o7.rOption = skillID;
                o7.tOption = 0;
                tsm.putCharacterStatValue(ElementalReset, o7);
                break;

        }
        getClient().write(new LP_TemporaryStatSet(tsm));
    }


    private int getArcaneAimSkill() {
        int res = 0;
        if (getCharacter().hasSkill(ARCANE_AIM_FP)) {
            res = ARCANE_AIM_FP;
        } else if (getCharacter().hasSkill(ARCANE_AIM_IL)) {
            res = ARCANE_AIM_IL;
        } else if (getCharacter().hasSkill(ARCANE_AIM_BISH)) {
            res = ARCANE_AIM_BISH;
        }
        return res;
    }

    private int getElementalDrainSkill() {
        int res = 0;
        if (getCharacter().hasSkill(FERVENT_DRAIN)) {
            res = FERVENT_DRAIN;
        } else if (getCharacter().hasSkill(ELEMENTAL_DRAIN)) {
            res = ELEMENTAL_DRAIN;
        }
        return res;
    }

    private void handleArcaneAim() {
        Skill skill = getCharacter().getSkill(getArcaneAimSkill());
        if (skill == null) {
            return;
        }
        SkillInfo arcaneAimInfo = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        int arcaneAimProp = arcaneAimInfo.getValue(prop, slv);
        if (!Rand.getChance(arcaneAimProp)) {
            return;
        }
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o = new Option();
        Option o1 = new Option();
        Option o2 = new Option();
        int amount = 1;
        if (tsm.hasStat(ArcaneAim)) {
            amount = tsm.getOption(ArcaneAim).nOption;
            if (amount < arcaneAimInfo.getValue(y, slv)) {
                amount++;
            }
        }
        o.nOption = amount;
        o.rOption = 2320011;
        o.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(ArcaneAim, o);
        o1.nOption = arcaneAimInfo.getValue(ignoreMobpdpR, slv);
        o1.rOption = 2320011;
        o1.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(IgnoreMobpdpR, o1);
        o2.nOption = (amount * arcaneAimInfo.getValue(x, slv));
        o2.rOption = 2320011;
        o2.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(DamR, o2);
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private void handleIgnite(AttackInfo attackInfo) {
        Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(WizardIgnite)) {
            SkillInfo igniteInfo = SkillData.getInstance().getSkillInfoById(IGNITE);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                if (Rand.getChance(igniteInfo.getValue(prop, 10))) {
                    AffectedArea aa = AffectedArea.getPassiveAA(IGNITE, (byte) 10);
                    aa.setMobOrigin((byte) 1);
                    aa.setCharID(chr.getId());
                    aa.setPosition(mob.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(igniteInfo.getRect()));
                    aa.setDelay((short) 2);
                    aa.setSkillID(IGNITE_AA);
                    chr.getField().spawnAffectedArea(aa);
                }
            }
        }
    }

    private void handleMegiddoFlameReCreation(int skillID, byte slv, AttackInfo attackInfo) {
//        TemporaryStatManager tsm = chr.getTemporaryStatManager();
//        SkillInfo si = SkillData.getSkillInfoById(MEGIDDO_FLAME);
//        int anglenum = new Random().nextInt(360);
//        int delaynum = new Random().nextInt(100); //Random delay between 0~150ms
//        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
//            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
//            int TW1prop = 85;//  SkillData.getSkillInfoById(SOUL_SEEKER_EXPERT).getValue(prop, slv);   //TODO Change
//            if (Util.succeedProp(TW1prop)) {
//                int mobID = mai.mobId;
//
//                int inc = ForceAtomEnum.DA_ORB_RECREATION.getInc();
//                int type = ForceAtomEnum.DA_ORB_RECREATION.getForceAtomType();
//                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 30, 5,
//                        anglenum, delaynum, (int) System.currentTimeMillis(), 1, 0,
//                        new Position(0, 0)); //Slightly behind the player
//                chr.getField().broadcast(CField.createForceAtom(true, chr.getId(), mobID, type,
//                        true, mobID, MEGIDDO_FLAME_ATOM, forceAtomInfo, new Rect(), 0, 300,
//                        mob.getPosition(), MEGIDDO_FLAME_ATOM, mob.getPosition()));
//            }
//        }
    }

    private void handleFreezingCrush(AttackInfo attackInfo, byte slv) {
        Character chr = getCharacter();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getInstance().getSkillInfoById(FROST_CLUTCH);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            //if (Util.succeedProp(si.getValue(prop, slv))) {
            Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
            MobTemporaryStat mts = mob.getTemporaryStat();
            o1.nOption = 1;
            o1.rOption = FROST_CLUTCH;
            o1.tOption = 15; //si.getValue(subTime, slv);
            o1.mOption = 2; //Should be Amount
            mts.addStatOptionsAndBroadcast(MobBuffStat.Speed, o1); // IDA says it's Speed, but it doesn't broadcast the Debuff over the mob
            //}
        }
    }

}
