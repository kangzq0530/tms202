package com.msemu.world.client.jobs.sengoku;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.client.character.MobAttackInfo;
import com.msemu.world.client.character.skills.Option;
import com.msemu.world.client.character.skills.Skill;
import com.msemu.world.client.character.skills.SkillInfo;
import com.msemu.world.client.character.skills.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.jobs.Job;
import com.msemu.world.client.life.AffectedArea;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.Summon;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgColor;
import com.msemu.world.enums.MobStat;
import com.msemu.world.enums.MoveAbility;
import com.msemu.world.network.packets.UserPool.FoxManEnterField;
import com.msemu.world.network.packets.WvsContext.TemporaryStatSet;

import static com.msemu.world.client.character.skills.CharacterTemporaryStat.*;
import static com.msemu.world.enums.SkillStat.*;

/**
 * Created by Weber on 2018/4/14.
 */
public class Kanna extends Job {

    public static final int HAKU = 40020109;

    public static final int RADIANT_PEACOCK = 42101003;
    public static final int NIMBUS_CURSE = 42101005;
    public static final int HAKU_REBORN = 42101002;

    public static final int KISHIN_SHOUKAN = 42111003; //summon
    public static final int BLOSSOM_BARRIER = 42111004; //AoE
    public static final int SOUL_SHEAR = 42111002; //Reactive Skill

    public static final int MONKEY_SPIRITS = 42120003; //Passive activation summon
    public static final int BELLFLOWER_BARRIER = 42121005; //AoE
    public static final int AKATUSKI_HERO_KANNA = 42121006;
    public static final int NINE_TAILED_FURY = 42121024; //Attacking Skill + Buff
    public static final int BINDING_TEMPEST = 42121004;

    public static final int VERITABLE_PANDEMONIUM = 42121052; //Immobility Debuff
    public static final int PRINCESS_VOW_KANNA = 42121053;
    public static final int BLACKHEARTED_CURSE = 42121054;

    //Haku Buffs
    public static final int HAKUS_GIFT = 42121020;
    public static final int FOXFIRE = 42121021;
    public static final int HAKUS_BLESSING = 42121022;
    public static final int BREATH_UNSEEN = 42121023;

    private int[] buffs = new int[]{
            HAKU_REBORN,
            RADIANT_PEACOCK,
            KISHIN_SHOUKAN,
            AKATUSKI_HERO_KANNA,
            NINE_TAILED_FURY,
            PRINCESS_VOW_KANNA,
            BLACKHEARTED_CURSE,
    };


    public Kanna(Character character) {
        super(character);
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
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case BINDING_TEMPEST:
            case VERITABLE_PANDEMONIUM:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }

                break;
            case NIMBUS_CURSE:
                AffectedArea aa = AffectedArea.getPassiveAA(skillID, (byte) slv);
                aa.setMobOrigin((byte) 0);
                aa.setCharID(chr.getId());
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                aa.setDelay((short) 5);
                chr.getField().spawnAffectedArea(aa);
                break;
        }
    }

    public void getHakuFollow() {
        //if(chr.hasSkill(HAKU)) {
        getClient().write(new FoxManEnterField(getCharacter()));
        //}
    }


    public void handleBuff(InPacket inPacket, int skillID, byte slv) {
        Character chr = getCharacter();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Summon summon;
        Field field;
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        switch (skillID) {
            case HAKU_REBORN:
                o1.nOption = 0;
                o1.rOption = skillID;
                o1.tOption = 30;
                tsm.putCharacterStatValue(ChangeFoxMan, o1);
                break;
            case RADIANT_PEACOCK:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                getHakuFollow();
                break;
            case KISHIN_SHOUKAN:
                summon = Summon.getSummonBy(getCharacter(), skillID, slv);
                field = getCharacter().getField();
                summon.setFlyMob(true);

                int x1 = chr.getPosition().deepCopy().getX() - 500;
                int x2 = chr.getPosition().deepCopy().getX() + 500;
                summon.setKishinPositions(new Position[]{new Position(x1, chr.getPosition().getY()), new Position(x2, chr.getPosition().getY())});

                summon.setMoveAbility(MoveAbility.STATIC.getVal());
                field.spawnAddSummon(summon);
                break;
            case AKATUSKI_HERO_KANNA:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case PRINCESS_VOW_KANNA:
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
            case BLACKHEARTED_CURSE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BlackHeartedCurse, o1);
                break;
        }
        getClient().write(new TemporaryStatSet(tsm));
    }


    @Override
    public void handleSkillPacket(int skillID, byte slv, InPacket inPacket) {
        TemporaryStatManager tsm = getCharacter().getTemporaryStatManager();
        Character chr = getCharacter();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        chr.chatMessage(ChatMsgColor.YELLOW, "SkillID: " + skillID);
        if (isBuff(skillID)) {
            handleBuff(inPacket, skillID, slv);
        } else {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            switch (skillID) {
                case BLOSSOM_BARRIER:
                case BELLFLOWER_BARRIER:
                    AffectedArea aa = AffectedArea.getPassiveAA(skillID, slv);
                    aa.setMobOrigin((byte) 0);
                    aa.setCharID(chr.getId());
                    aa.setPosition(chr.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                    aa.setDelay((short) 3);
                    chr.getField().spawnAffectedArea(aa);
                    break;
                case NINE_TAILED_FURY:
                    o1.nReason = skillID;
                    o1.nValue = si.getValue(indieDamR, slv);
                    o1.tStart = (int) System.currentTimeMillis();
                    o1.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieDamR, o1); //Indie
                    getClient().write(new TemporaryStatSet(tsm));
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
        return false;
    }

    @Override
    public void handleHitPacket(InPacket inPacket, HitInfo hitInfo) {

    }
}
