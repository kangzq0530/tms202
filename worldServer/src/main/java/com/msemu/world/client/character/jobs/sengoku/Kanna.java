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

package com.msemu.world.client.character.jobs.sengoku;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.packets.outpacket.user.LP_FoxManEnterField;
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
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.MoveAbility;

import static com.msemu.commons.data.enums.SkillStat.*;
import static com.msemu.world.client.character.stats.CharacterTemporaryStat.*;

/**
 * Created by Weber on 2018/4/14.
 */
public class Kanna extends JobHandler {

    public static final int 花狐 = 40020109;

    public static final int 扇_孔雀 = 42101003;
    public static final int 妖雲召喚 = 42101005;
    public static final int 影朋_花狐 = 42101002;

    public static final int 鬼神召喚 = 42111003; //summon
    public static final int 結界_櫻 = 42111004; //AoE
    public static final int 紅玉咒印 = 42111002; //Reactive Skill

    public static final int 猩猩火酒 = 42120003; //Passive activation summon
    public static final int 結界_桔梗 = 42121005; //AoE
    public static final int 曉月勇者 = 42121006;
    public static final int 紫扇白狐 = 42121024; //Attacking Skill + Buff
    public static final int 退魔流星符 = 42121004;

    public static final int 百鬼夜行 = 42121052; //Immobility Debuff
    public static final int 公主的加護 = 42121053;
    public static final int 結界_破魔 = 42121054;

    //Haku Buffs
    public static final int HAKUS_GIFT = 42121020;
    public static final int FOXFIRE = 42121021;
    public static final int HAKUS_BLESSING = 42121022;
    public static final int BREATH_UNSEEN = 42121023;

    private int[] buffs = new int[]{
            影朋_花狐,
            扇_孔雀,
            鬼神召喚,
            曉月勇者,
            紫扇白狐,
            公主的加護,
            結界_破魔,
    };


    public Kanna(Character character) {
        super(character);
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


        if (normalAttack) return true;

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();

        switch (skillID) {
            case 退魔流星符:
            case 百鬼夜行:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = chr.getField().getMobByObjectId(mai.getObjectID());
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobBuffStat.Stun, o1);
                }

                break;
            case 妖雲召喚:
                AffectedArea aa = AffectedArea.getPassiveAA(skillID, (byte) slv);
                aa.setMobOrigin((byte) 0);
                aa.setCharID(chr.getId());
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRect()));
                aa.setDelay((short) 5);
                chr.getField().spawnAffectedArea(aa);
                break;
        }
        return true;
    }

    public void getHakuFollow() {
        //if(chr.hasSkill(花狐)) {
        getClient().write(new LP_FoxManEnterField(getCharacter()));
        //}
    }


    public void handleBuff(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();

        final byte slv = skillUseInfo.getSlv();
        final Character chr = getCharacter();
        final TemporaryStatManager tsm = chr.getTemporaryStatManager();
        final SkillInfo si = getSkillInfo(skillID);
        Summon summon;
        Field field;
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        switch (skillID) {
            case 影朋_花狐:
                o1.nOption = 0;
                o1.rOption = skillID;
                o1.tOption = 30;
                tsm.putCharacterStatValue(ChangeFoxMan, o1);
                break;
            case 扇_孔雀:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                getHakuFollow();
                break;
            case 鬼神召喚:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                field = getCharacter().getField();
                summon.setFlyMob(true);

                int x1 = chr.getPosition().deepCopy().getX() - 500;
                int x2 = chr.getPosition().deepCopy().getX() + 500;
                summon.setKishinPositions(new Position[]{new Position(x1, chr.getPosition().getY()), new Position(x2, chr.getPosition().getY())});
                summon.setMoveAbility(MoveAbility.STATIC.getVal());
                field.spawnLife(summon);
                break;
            case 曉月勇者:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case 公主的加護:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOver, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOver, o2);
                break;
            case 結界_破魔:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BlackHeartedCurse, o1);
                break;
        }
        getClient().write(new LP_TemporaryStatSet(tsm));
    }


    @Override
    public boolean handleSkillUse(SkillUseInfo skillUseInfo) {
        final int skillID = skillUseInfo.getSkillID();
        final byte slv = skillUseInfo.getSlv();
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Character chr = getCharacter();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getInstance().getSkillInfoById(skillID);
        }
        chr.chatMessage(ChatMsgType.NOTICE, "SkillID: " + skillID);
        if (isBuff(skillID)) {
            handleBuff(skillUseInfo);
        } else {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            switch (skillID) {
                case 結界_櫻:
                case 結界_桔梗:
                    AffectedArea aa = AffectedArea.getPassiveAA(skillID, slv);
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    aa.setPosition(chr.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(si.getRect()));
                    aa.setDelay((short) 3);
                    chr.getField().spawnAffectedArea(aa);
                    break;
                case 紫扇白狐:
                    o1.nReason = skillID;
                    o1.nValue = si.getValue(indieDamR, slv);
                    o1.tStart = (int) System.currentTimeMillis();
                    o1.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieDamR, o1); //Indie
                    getClient().write(new LP_TemporaryStatSet(tsm));
                    break;
            }
        }
        return true;
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
        return false;
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {

    }
}
