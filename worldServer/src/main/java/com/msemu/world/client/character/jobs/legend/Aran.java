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

package com.msemu.world.client.character.jobs.legend;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_ModCombo;
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
import com.msemu.world.client.field.lifes.skills.MobTemporaryStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.Stat;

import java.util.Arrays;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.stats.CharacterTemporaryStat.*;

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

    public static final int 旋風斬 = 21101017;
    public static final int 旋風斬_COMBO = 21100013; //Special Attack (Stun Debuff) (Special Skill from Key-Command)

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

    public void handleBuff(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = getSkillInfo(skillID);
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
        final Character chr = getCharacter();
        final SkillInfo adrenalinInfo = SkillData.getInstance().getSkillInfoById(鬥氣爆發);
        if (getCharacter().hasSkill(鬥氣爆發)) {
            Option o = new Option();
            o.nOption = 1;
            o.rOption = 鬥氣爆發;
            o.tOption = adrenalinInfo.getValue(time, chr.getSkill(鬥氣爆發).getCurrentLevel());
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

    private int getCombo() {
        return combo;
    }

    private void setCombo(int combo) {
        this.combo = combo;
        this.showCombo();
    }

    public void incCombo() {
        setCombo(getCombo() + 1);
    }

    public void showCombo() {
        getClient().write(new LP_ModCombo(getCombo()));
    }

    @Override
    public boolean handleAttack(AttackInfo attackInfo) {
        final boolean normalAttack = attackInfo.skillId == 0;
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final int linkedSkill = SkillConstants.getLinkedSkill(attackInfo.skillId);
        final boolean hasSkill = chr.getSkill(linkedSkill > 0 ? linkedSkill : attackInfo.skillId) != null;
        if (!hasSkill)
            return false;
        final SkillInfo si = getSkillInfo(attackInfo.skillId);
        if ((!normalAttack) && si == null)
            return false;
        final int slv = chr.getSkill(linkedSkill > 0 ? linkedSkill : attackInfo.skillId).getCurrentLevel();
        if ((!normalAttack) && attackInfo.slv != slv)
            return false;
        if (normalAttack && slv != 0)
            return false;
        final boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        final int skillId = attackInfo.skillId;
        // trigger cheat attack
        if (hasHitMobs) {
            handleComboAbility(tsm, attackInfo);
            if (chr.hasSkill(鬥氣爆發)) {
                if (tsm.getOption(ComboAbilityBuff).nOption > 999) {
                    if (chr.hasSkill(鬥氣爆發)) {
                        tsm.getOption(ComboAbilityBuff).nOption = 1000;
                        handleAdrenalinRush(skillId, tsm);
                        getClient().write(new LP_TemporaryStatSet(tsm));
                        comboAfterAdrenalin();
                    }
                }
            }
        }
        // 觸發寒冰屬性
        if (hasHitMobs) {
            if (tsm.hasStat(WeaponCharge) && tsm.getOption(WeaponCharge).rOption == 寒冰屬性) {
                final SkillInfo iceSklllInfo = SkillData.getInstance().getSkillInfoById(寒冰屬性);
                final Skill iceSkill = chr.getSkill(寒冰屬性);
                attackInfo.getMobAttackInfo().forEach(mai -> {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    if (mob != null && mob.getTemporaryStat().hasCurrentMobStat(MobBuffStat.Speed)) {
                        return;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    Option option = new Option();
                    option.nOption = iceSklllInfo.getValue(SkillStat.x, iceSkill.getCurrentLevel());
                    option.rOption = 寒冰屬性;
                    option.tOption = iceSklllInfo.getValue(SkillStat.y, iceSkill.getCurrentLevel());
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Speed, option);
                });
            }
        }

        // 觸發吸血術

        if (hasHitMobs) {
            if (chr.hasSkill(吸血術)) {
                if (tsm.hasStat(AranDrain)) {
                    final SkillInfo aranDrainInfo = SkillData.getInstance().getSkillInfoById(吸血術);
                    final Skill aranDrain = chr.getSkill(吸血術);
                    attackInfo.getMobAttackInfo().forEach(mai -> {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        if (mob == null)
                            return;
                        final int maxHp = mob.getMaxMp();
                        final int x = aranDrainInfo.getValue(SkillStat.x, aranDrain.getCurrentLevel());
                        final int healHp = (int) ((maxHp * x) / 100.0);
                        chr.addStat(Stat.HP, healHp);
                    });
                }
            }
        }

        if (normalAttack) return true;
        handleSwingStudies(getOriginalSkillByID(skillId), tsm);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillId) {
            case 極速巔峰_目標鎖定:
                int t = si.getValue(subTime, slv);
                o1.nOption = 1;
                o1.rOption = skillId;
                o1.tOption = t;
                tsm.putCharacterStatValue(AranBoostEndHunt, o1);
                break;
            case 突刺之矛_COMBO:
                final SkillInfo fcComboInfo = SkillData.getInstance().getSkillInfoById(21100002);
                final SkillInfo fcInfo = SkillData.getInstance().getSkillInfoById(21101011);
                final Skill fcSkill = chr.getSkill(21101011);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int x = fcComboInfo.getValue(SkillStat.x, fcSkill.getCurrentLevel());
                    int hcProp = fcComboInfo.getValue(SkillStat.hcProp, fcSkill.getCurrentLevel());
                    int hcTime = fcComboInfo.getValue(SkillStat.hcTime, fcSkill.getCurrentLevel());
                    if (Rand.getChance(hcProp)) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = x;
                        o1.rOption = fcInfo.getSkillId();
                        o1.tOption = hcTime;
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            case 旋風斬_COMBO:
            case 終極之矛_COMBO:
            case 終極之矛_粉碎震撼_COMBO: {
                final SkillInfo psdInfo = SkillData.getInstance().getSkillInfoById(skillId);
                final Skill oriSkill = chr.getSkill(linkedSkill);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int prop = psdInfo.getValue(SkillStat.prop, oriSkill.getCurrentLevel());
                    int time = psdInfo.getValue(SkillStat.time, oriSkill.getCurrentLevel());
                    if (Rand.getChance(prop)) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = oriSkill.getSkillId();
                        o1.tOption = time;
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                    }
                }
                break;
            }
            case 鬥氣審判_COMBO_下:
            case 鬥氣審判_COMBO_左:
            case 鬥氣審判_COMBO_右: {
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    final SkillInfo psdInfo = SkillData.getInstance().getSkillInfoById(skillId);
                    final Skill judgeSkill = chr.getSkill(鬥氣審判);
                    int hcProp = psdInfo.getValue(SkillStat.hcProp, judgeSkill.getCurrentLevel());
                    int hcTime = psdInfo.getValue(SkillStat.hcTime, judgeSkill.getCurrentLevel());
                    int x = psdInfo.getValue(SkillStat.x, judgeSkill.getCurrentLevel());
                    if (Rand.getChance(hcProp)) {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = 鬥氣審判;
                        o1.tOption = hcTime;
                        mts.addStatOptionsAndBroadcast(MobBuffStat.Freeze, o1);
                    } else {
                        Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr.getId(), chr.getSkill(鬥氣審判), 1);
                    }
                }
                break;
            }
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
        chr.chatMessage(ChatMsgType.NOTICE, "SkillID: " + skillID);
        if (isBuff(skillID)) {
            handleBuff(skillUseInfo);
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
                    aa.setRect(aa.getPosition().getRectAround(mdi.getRect(slv)));
                    chr.getField().spawnAffectedArea(aa);
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return MapleJob.is狂狼勇士(id);
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
