package com.msemu.world.client.character.jobs.legend;

import com.msemu.commons.data.enums.MobStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.packets.out.wvscontext.LP_ModCombo;
import com.msemu.core.network.packets.out.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.MobAttackInfo;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.skill.Option;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.skill.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.life.AffectedArea;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.Stat;

import java.util.Arrays;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.skill.CharacterTemporaryStat.*;

/**
 * Created by Weber on 2018/4/13.
 */
public class Aran extends JobHandler {

    public static final int 找回記憶 = 20000194;
    public static final int 戰鬥衝刺 = 20001295;
    public static final int 返回瑞恩 = 20001296;

    public static final int 矛之鬥氣 = 21000000;
    public static final int 神速之矛 = 21001003;
    public static final int 強化連擊 = 21001008;

    public static final int 吸血術 = 21101005;
    public static final int 寒冰屬性 = 21101006;

    public static final int 瑪哈祝福 = 21111012;
    public static final int 鬥氣爆發 = 21110016;
    public static final int 空中震撼 = 21110026;

    public static final int 終極攻擊 = 21100010;
    public static final int 進階終極攻擊 = 21120012;


    public static final int 楓葉祝福 = 21121000;
    public static final int 英雄誓言 = 21121053;
    public static final int 腎上腺動力源 = 21121058;
    public static final int 瑪哈的領域 = 21121068;

    /// 攻擊技能
    public static final int 猛擲之矛 = 21001009;
    public static final int 猛擲之矛_COMBO = 21000004;

    public static final int 粉碎震撼_1 = 21001010;
    public static final int 粉碎震撼_2 = 21000006;
    public static final int 粉碎震撼_3 = 21000007;
    public static final int 粉碎震撼_2_終極之矛 = 21120025;

    public static final int 突刺之矛 = 21101011;
    public static final int 突刺之矛_COMBO = 21100002; //Special Attack (Stun Debuff) (Special Skill from Key-Command)

    public static final int 終極研究I = 21100015;
    public static final int FINAL_TOSS_COMBO = 21100012;

    public static final int ROLLING_SPIN = 21101017;
    public static final int ROLLING_SPIN_COMBO = 21100013; //Special Attack (Stun Debuff) (Special Skill from Key-Command)

    public static final int GATHERING_HOOK = 21111019;
    public static final int GATHERING_HOOK_COMBO = 21110018;

    public static final int 終極之矛 = 21111021;
    public static final int 終極之矛_COMBO = 21110020; //Special Attack (Stun Debuff) (Special Skill from Key-Command)
    public static final int 終極之矛_粉碎震撼_COMBO = 21110028; //Special Attack (Stun Debuff) (Special Skill from Key-Command)
    public static final int 終極之矛_ADRENALINE_SHOCKWAVE = 21110027; //Shockwave after final blow when in Adrenaline Rush

    public static final int 鬥氣審判 = 21111017;
    public static final int 鬥氣審判_COMBO_下 = 21110011; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)
    public static final int 鬥氣審判_COMBO_左 = 21110024; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)
    public static final int 鬥氣審判_COMBO_右 = 21110025; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)

    public static final int 比耀德_1 = 21120022;
    public static final int 比耀德_2 = 21121016;
    public static final int 比耀德_3 = 21121017;

    public static final int 極速巔峰_目標鎖定 = 21120019;


    private int[] buffs = new int[]{
            神速之矛,
            強化連擊,
            寒冰屬性,
            瑪哈祝福,
            楓葉祝福,
            吸血術,
            英雄誓言,
    };

    private int[] addedSkills = new int[]{
            返回瑞恩,
            找回記憶,
            戰鬥衝刺,
    };

    public static int getOriginalSkillByID(int skillID) {
        switch (skillID) {
            case 猛擲之矛_COMBO:
                return 猛擲之矛;
            case 終極之矛_COMBO:
            case 終極之矛_粉碎震撼_COMBO:
                return 終極之矛;
            case 粉碎震撼_2:
            case 粉碎震撼_3:
                return 粉碎震撼_1;
        }
        return skillID; // no original skill linked with this one
    }

    private int combo = 0;

    public Aran(Character character) {
        super(character);
        if (isHandlerOfJob(character.getJob())) {
            Arrays.stream(addedSkills).forEach(skillId -> {
                if (!character.hasSkill(skillId)) {
                    Skill skill = SkillData.getInstance().getSkillById(skillId);
                    if (skill == null)
                        return;
                    skill.setCurrentLevel(skill.getMasterLevel());
                    character.addSkill(skill);
                }
            });
        }
    }

    public void handleBuff(InPacket packet, int skillID, byte slv) {
        Character character = getCharacter();
        SkillInfo si = SkillData.getInstance().getSkillInfoById(skillID);
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case 神速之矛:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case 強化連擊:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(BodyPressure, o1);
                break;
            case 吸血術:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(AranDrain, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(mhpR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = 0;
                tsm.putCharacterStatValue(IndieMHPR, o2);
                break;
            case 寒冰屬性:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(WeaponCharge, o1); //TODO  Finish gives slow debuff to mobs and half duration to bosses
                break;
            case 瑪哈祝福:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;
            case 楓葉祝福:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;

            case 英雄誓言:
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
        }
        getClient().write(new LP_TemporaryStatSet(tsm));
    }

    private void handleComboAbility(TemporaryStatManager tsm, AttackInfo attackInfo) {
        Option o = new Option();
        SkillInfo comboInfo = SkillData.getInstance().getSkillInfoById(矛之鬥氣);
        int amount = 1;
        if (!getCharacter().hasSkill(矛之鬥氣)) {
            return;
        }
        if (tsm.hasStat(ComboAbilityBuff)) {
            amount = tsm.getOption(ComboAbilityBuff).nOption;
            if (amount < comboInfo.getValue(s2, getCharacter().getSkill(矛之鬥氣).getCurrentLevel())) {
                amount = attackInfo.mobAttackInfo.size();
            }

        }
        o.nOption = amount;
        o.rOption = 矛之鬥氣;
        o.tOption = 0;
        tsm.putCharacterStatValue(ComboAbilityBuff, o);
        setCombo(amount);
    }

    private void handleAdrenalinRush(int skillId, TemporaryStatManager tsm) {
        // 處理鬥氣爆發
        SkillInfo adrenalinInfo = SkillData.getInstance().getSkillInfoById(鬥氣爆發);
        if (getCharacter().hasSkill(鬥氣爆發)) {
            Option o = new Option();
            o.nOption = 1;
            o.rOption = 鬥氣爆發;
            o.tOption = adrenalinInfo.getValue(time, adrenalinInfo.getCurrentLevel());
            o.cOption = 1;
            tsm.putCharacterStatValue(AdrenalinBoost, o);
            getClient().write(new LP_TemporaryStatSet(tsm));
        }
    }

    private void comboAfterAdrenalin() {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        tsm.getOption(ComboAbilityBuff).nOption = 500;
    }

    private void handleSwingStudies(int skillId, TemporaryStatManager tsm) {
        Option o = new Option();
        if (getCharacter().hasSkill(終極研究I)) {
            o.nOption = 1;
            o.rOption = 終極研究I;
            o.tOption = 5;
            tsm.putCharacterStatValue(NextAttackEnhance, o);
            getClient().write(new LP_TemporaryStatSet(tsm));
        }
    }

    private void setCombo(int combo) {
        this.combo = combo;
        getClient().write(new LP_ModCombo(getCombo()));
    }

    private int getCombo() {
        return combo;
    }

    @Override
    public void handleAttack(AttackInfo attackInfo) {
        Character character = getCharacter();
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Skill skill = getCharacter().getSkill(attackInfo.skillId);
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
            handleComboAbility(tsm, attackInfo);
            if (character.hasSkill(鬥氣爆發)) {
                if (tsm.getOption(ComboAbilityBuff).nOption > 999) {
                    if (character.hasSkill(鬥氣爆發)) {
                        tsm.getOption(ComboAbilityBuff).nOption = 1000;
                        handleAdrenalinRush(skillID, tsm);
                        getClient().write(new LP_TemporaryStatSet(tsm));
                        comboAfterAdrenalin();
                    }
                }
            }
        }
        handleSwingStudies(getOriginalSkillByID(skillID), tsm);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case 極速巔峰_目標鎖定:
                int t = si.getValue(subTime, slv);
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = t;
                tsm.putCharacterStatValue(AranBoostEndHunt, o1);
                getClient().write(new LP_TemporaryStatSet(tsm));
                break;
            case 突刺之矛_COMBO: //TODO  Leaves an ice trail behind that freezes enemies
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int hcProp = 5; //hcProp is defined yet still gives NPEs
                    int hcTime = 10; //hcTime is defined yet still gives NPEs
                    if (Rand.getChance(hcProp)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = hcTime;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case ROLLING_SPIN_COMBO:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int prop = 30; //Prop value never given, so I decided upon 30%.
                    int time = 3; //Time value never given, so I decided upon 3 seconds.
                    if (Rand.getChance(prop)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = time;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case 終極之矛_COMBO:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int prop = 30; //Prop value never given, so I decided upon 30%.
                    int time = 3; //Time value never given, so I decided upon 3 seconds.
                    if (Rand.getChance(prop)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = time;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case 終極之矛_粉碎震撼_COMBO:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int prop = 30; //Prop value never given, so I decided upon 30%.
                    int time = 3; //Time value never given, so I decided upon 3 seconds.
                    if (Rand.getChance(prop)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = time;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case 鬥氣審判_COMBO_下:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int hcProp = 5; //hcProp is defined yet still gives NPEs
                    int hcTime = 2; //hcTime is defined yet still gives NPE
                    if (Rand.getChance(hcProp/*si.getValue(hcProp, slv)*/)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = hcTime;    //si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                    } else {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(character.getId(), skill, 1);
                    }
                }
                break;
            case 鬥氣審判_COMBO_左:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int hcProp = 5; //hcProp is defined yet still gives NPE
                    int hcTime = 2; //hcTime is defined yet still gives NPE
                    if (Rand.getChance(hcProp/*si.getValue(hcProp, slv)*/)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = hcTime;    //si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                    } else {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(character.getId(), skill, 1);
                    }
                }
                break;
            case 鬥氣審判_COMBO_右:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int hcProp = 5; //hcProp is defined yet still gives NPEs
                    int hcTime = 2; //hcTime is defined yet still gives NPE
                    if (Rand.getChance(hcProp/*si.getValue(hcProp, slv)*/)) {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = getOriginalSkillByID(skillID);
                        o1.tOption = hcTime;    //si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                    } else {
                        Mob mob = (Mob) character.getField().getLifeByObjectID(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(character.getId(), skill, 1);
                    }
                }
                break;
        }
    }

    @Override
    public void handleSkillPacket(int skillID, byte slv, InPacket inPacket) {
        Character chr = getCharacter();
        Skill skill = chr.getSkill(skillID);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getInstance().getSkillInfoById(skillID);
        }
        chr.chatMessage(ChatMsgType.YELLOW, "SkillID: " + skillID);
        if (isBuff(skillID)) {
            handleBuff(inPacket, skillID, slv);
        } else {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            switch (skillID) {
                case 腎上腺動力源:
                    tsm.getOption(ComboAbilityBuff).nOption = 1000;
                    handleAdrenalinRush(skillID, tsm);
                    getClient().write(new LP_TemporaryStatSet(tsm));
                    break;
                case 返回瑞恩:
                    o1.nValue = si.getValue(x, slv);
                    Field toField = getClient().getChannelInstance().getField(o1.nValue);
                    chr.warp(toField);
                    break;
                case 瑪哈的領域:
                    SkillInfo mdi = SkillData.getInstance().getSkillInfoById(瑪哈的領域);
                    AffectedArea aa = AffectedArea.getPassiveAA(skillID, slv);
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    aa.setPosition(chr.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(mdi.getRects().get(0)));
                    chr.getField().spawnAffectedArea(aa);
                    break;
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return false;
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

    }

    @Override
    public void handleLevelUp() {
        int addedMaxHp = Rand.get(50, 52);
        int addedMaxMp = Rand.get(4, 6);
        getCharacter().addStat(Stat.AP, 5);
        getCharacter().addStat(Stat.MAX_HP, addedMaxHp);
        getCharacter().addStat(Stat.MAX_MP, addedMaxMp);
    }
}
