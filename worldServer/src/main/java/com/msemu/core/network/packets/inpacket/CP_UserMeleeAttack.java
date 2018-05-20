package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserMeleeAttack;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.MobAttackInfo;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.Stat;

import java.util.Arrays;

/**
 * Created by Weber on 2018/5/4.
 */
public class CP_UserMeleeAttack extends InPacket<GameClient> {

    private AttackInfo ai;

    public CP_UserMeleeAttack(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        ai = new AttackInfo();
        ai.isMeleeAttack = true;
        ai.setFieldKey(decodeByte());
        Byte atkMask = decodeByte();
        ai.setHits((byte) (atkMask & 0xF));
        ai.setMobCount(atkMask >>> 4);
        ai.setSkillId(decodeInt());
        ai.setSlv(decodeByte());
        ai.setAddAttackProc(decodeByte());
        ai.setCrc(decodeInt());
        final int skillID = ai.skillId;
        if (SkillConstants.isKeyDownSkill(skillID) || SkillConstants.isSuperNovaSkill(skillID)) {
            ai.setKeyDown(decodeInt());
        }
        if (SkillConstants.isRushBombSkill(skillID) || skillID == 5300007 || skillID == 27120211 || skillID == 14111023) {
            ai.setGrenadeId(decodeInt());
        }
        if (SkillConstants.isZeroSkill(skillID)) {
            ai.zero = decodeByte();
        }
        if (SkillConstants.isUsercloneSummonedAbleSkill(skillID)) {
            ai.bySummonedID = decodeInt();
        }
        skip(13);
        ai.setBuckShot(decodeByte());
        ai.setSomeMask(decodeByte());
        Short actionMask = decodeShort();
        ai.setLeft(((actionMask >> 15) & 1) != 0);
        ai.setAttackAction((short) (actionMask & 0x7FFF));
        decodeInt(); // crc
        ai.setAttackActionType(decodeByte());
        ai.setAttackSpeed(decodeByte());
        ai.setTick(decodeInt());
        ai.getPtTarget().setY(decodeInt());
        ai.setFinalAttackLastSkillID(decodeInt());
        if (ai.getFinalAttackLastSkillID() > 0) {
            ai.setFinalAttackByte(decodeByte());
        }
        if (skillID == 5111009) {
            ai.ignorePCounter = decodeByte() != 0;
        }
        if (skillID == 25111005) {
            ai.spiritCoreEnhance = decodeInt();
        }
        for (int i = 0; i < ai.mobCount; i++) {
            MobAttackInfo mai = new MobAttackInfo();
            int mobObjectID = decodeInt();
            byte idk1 = decodeByte();
            byte idk2 = decodeByte();
            byte idk3 = decodeByte();
            byte idk4 = decodeByte();
            byte idk5 = decodeByte();
            int templateID = decodeInt();
            byte calcDamageStatIndex = decodeByte();
            short rcDstX = decodeShort();
            short rectRight = decodeShort();
            short idk6 = decodeShort();
            short oldPosX = decodeShort(); // ?
            short oldPosY = decodeShort(); // ?
            decodeInt();
            long[] damages = new long[ai.hits];
            for (int j = 0; j < ai.hits; j++) {
                damages[j] = decodeLong();
            }
            int mobUpDownYRange = decodeInt();
            decodeInt(); // crc
            decodeInt();
            boolean isResWarriorLiftPress = false;
            if (skillID == 37111005) {
                isResWarriorLiftPress = decodeByte() != 0;
            }
            // Begin PACKETMAKER::MakeAttackInfoPacket
            byte type = decodeByte();
            String currentAnimationName = "";
            int animationDeltaL = 0;
            String[] hitPartRunTimes = new String[0];
            if (type == 1) {
                currentAnimationName = decodeString();
                animationDeltaL = decodeInt();
                int hitPartRunTimesSize = decodeInt();
                hitPartRunTimes = new String[hitPartRunTimesSize];
                for (int j = 0; j < hitPartRunTimesSize; j++) {
                    hitPartRunTimes[j] = decodeString();
                }
            } else if (type == 2) {
                currentAnimationName = decodeString();
                animationDeltaL = decodeInt();
            }
            skip(4);
            skip(4);
            skip(1);
            // End PACKETMAKER::MakeAttackInfoPacket
            mai.setObjectID(mobObjectID);
            mai.setIdk1(idk1);
            mai.setIdk2(idk2);
            mai.setIdk3(idk3);
            mai.setIdk4(idk4);
            mai.setIdk5(idk5);
            mai.setTemplateID(templateID);
            mai.setCalcDamageStatIndex(calcDamageStatIndex);
            mai.setRcDstX(rcDstX);
            mai.setRectRight(rectRight);
            mai.setOldPosX(oldPosX);
            mai.setOldPosY(oldPosY);
            mai.setIdk6(idk6);
            mai.setDamages(damages);
            mai.setMobUpDownYRange(mobUpDownYRange);
            mai.setType(type);
            mai.setCurrentAnimationName(currentAnimationName);
            mai.setAnimationDeltaL(animationDeltaL);
            mai.setHitPartRunTimes(hitPartRunTimes);
            mai.setResWarriorLiftPress(isResWarriorLiftPress);
            ai.getMobAttackInfo().add(mai);
        }
        if (skillID == 61121052 || skillID == 36121052 || SkillConstants.isScreenCenterAttackSkill(skillID)) {
            ai.ptTarget.setX(decodeShort());
            ai.ptTarget.setY(decodeShort());
        } else {
            if (SkillConstants.isSuperNovaSkill(skillID)) {
                ai.ptAttackRefPoint.setX(decodeShort());
                ai.ptAttackRefPoint.setY(decodeShort());
            }
            if (skillID == 101000102) {
                ai.idkPos.setX(decodeShort());
                ai.idkPos.setY(decodeShort());
            }
            ai.pos.setX(decodeShort());
            ai.pos.setY(decodeShort());
            if (SkillConstants.isAranFallingStopSkill(skillID)) {
                ai.fh = decodeByte();
            }
            if (skillID == 21120019 || skillID == 37121052) {
                ai.teleportPt.setX(decodeInt());
                ai.teleportPt.setY(decodeInt());
            }
            if (skillID == 61121105 || skillID == 61121222 || skillID == 24121052) {
                ai.Vx = decodeShort();
                short x, y;
                for (int i = 0; i < ai.Vx; i++) {
                    x = decodeShort();
                    y = decodeShort();
                }
            }
            if (skillID == 101120104) {
                // CUser::EncodeAdvancedEarthBreak
                // TODO
            }
            if (skillID == 14111006 && ai.grenadeId != 0) {
                ai.grenadePos.setX(decodeShort());
                ai.grenadePos.setY(decodeShort());
            }
            if (available() != 0) {
                getClient().getCharacter().chatMessage("CP_MeleeAttack 解包不完整");
            }

        }
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        final Skill skill = chr.getSkill(ai.getSkillId());
        SkillInfo si = skill != null ? SkillData.getInstance().getSkillInfoById(ai.getSkillId()) : null;
        boolean attackSuccess = chr.getJobHandler().handleAttack(ai);
        field.broadcastPacket(new LP_UserMeleeAttack(chr, ai));

        if (attackSuccess) {
            if (skill != null) {
                final int hpCon = si.getValue(SkillStat.hpCon, ai.getSlv());
                final int mpCon = si.getValue(SkillStat.mpCon, ai.getSlv());
                final int hpRCon = si.getValue(SkillStat.hpRCon, ai.getSlv());
                if (hpCon > 0)
                    chr.addStat(Stat.HP, -hpCon);
                if (mpCon > 0)
                    chr.addStat(Stat.MP, -mpCon);
                if (hpRCon > 0)
                    chr.addStat(Stat.HP, (int) -(chr.getStat(Stat.HP) * (chr.getStat(Stat.HP) * (hpRCon /100.0))));
            }
            chr.renewCharacterStats();
            ai.getMobAttackInfo().forEach(mai -> {
                Mob mob = field.getMobByObjectId(mai.getObjectID());
                if (mob != null) {
                    long totalDamage = Arrays.stream(mai.getDamages()).sum();
                    chr.chatMessage(ChatMsgType.GAME_DESC, String.format("近距離攻擊: 技能: %s(%d) 怪物: %s(%d)  HP: (%d/%d) MP: (%d/%d) 總攻擊: %d 座標: %s",
                            (si != null ? si.getName() : "普通攻擊"), ai.getSkillId(), mob.getTemplate().getName(),
                            mob.getTemplateId(), mob.getHp(), mob.getMaxHp(), mob.getMp(), mob.getMaxHp(), totalDamage, ai.getPos().toString()));
                    mob.addDamage(chr, totalDamage);
                    mob.damage(totalDamage);
                }
            });
        }
    }
}
