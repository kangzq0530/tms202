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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserMeleeAttack;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserShootAttack;
import com.msemu.world.Channel;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/4.
 */
public class CP_UserShootAttack extends InPacket<GameClient> {

    private AttackInfo attackInfo;

    public CP_UserShootAttack(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        /*
        00
        01
        31
        44 6F 40 01
        05
        00
        FB 06 E7 49

        00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A7 00 BD 3A 5C AE 02 08 67 A4 5D 04 00 00 00 00 00 00 00 5D 01 0E FF B5 03 87 FF 46 42 0F 00 07 00 00 00 03 23 87 01 00 01 FE 01 2A FF FC 01 2A FF C7 01 00 00 00 00 C6 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 E4 01 FC FE 0E 02 22 FF E7 23 70 FE 4D 42 0F 00 07 00 00 80 00 23 87 01 00 01 B5 02 2A FF B8 02 2A FF 62 02 00 00 00 00 E0 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 A4 02 06 FF D5 02 2A FF 6B 1E B6 B7 45 42 0F 00 07 00 00 80 05 23 87 01 00 01 39 03 2A FF 3B 03 2A FF E4 02 00 00 00 00 46 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 28 03 FE FE 59 03 21 FF 94 E3 70 7D 5D 01 66 FF

-----------------
        00
        02
        31
        44 6F 40 01
        05
        00
        FB 06 E7 49 - crc

        00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A7 00 BD 3A 5C AE 02 08 C7 0D 2B 04 00 00 00 00 00 00 00 65 01 0E FF BD 03 87 FF 66 42 0F 00 07 00 00 80 01 23 87 01 00 01 6C 02 2A FF 6E 02 2A FF 14 02 00 00 00 00 92 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 5B 02 08 FF 8B 02 2A FF D2 CB 12 69 63 42 0F 00 07 00 00 00 03 23 87 01 00 01 07 03 2A FF 04 03 2A FF A1 02 00 00 00 00 71 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 EC 02 FC FE 17 03 22 FF 1E 37 77 89 50 42 0F 00 07 00 00 00 02 23 87 01 00 01 46 03 2A FF 43 03 2A FF DD 02 00 00 00 00 44 00 00 00 00 00 00 00 00 00 00 00 FB 4A 1C C4 00 01 26 03 0A FF 57 03 2A FF 17 87 2F 1D 65 01 66 FF

         */
        attackInfo = new AttackInfo();
        attackInfo.isShootAttack = true;

        // if(nCriticalDamage) ??
        skip(1);
        attackInfo.setFieldKey(decodeByte());
        Byte atkMask = decodeByte();
        attackInfo.setHits((byte) (atkMask & 0xF));
        attackInfo.setMobCount((atkMask & 0xF0) >> 4);
        attackInfo.setSkillId(decodeInt());
        attackInfo.setSlv(decodeByte());
        attackInfo.setAddAttackProc(decodeByte());
        attackInfo.setCrc(decodeInt());
        if (SkillConstants.isKeyDownSkill(attackInfo.skillId)) {
            attackInfo.setKeyDown(decodeInt());
        }
        if (SkillConstants.isZeroSkill(attackInfo.skillId)) {
            attackInfo.setZero(decodeByte());
        }
        if (SkillConstants.isUsercloneSummonedAbleSkill(attackInfo.skillId)) {
            attackInfo.setBySummonedID(decodeInt());
        }

        skip(13);
        attackInfo.setBuckShot(decodeByte());
        attackInfo.setSomeMask(decodeByte());
        final boolean addtionalBolt = decodeInt() > 0;
        final boolean canApplyShootExJablin = decodeByte() > 0;
        attackInfo.setJablin(canApplyShootExJablin);
        final short mask = decodeShort();
        attackInfo.setLeft(((mask >>> 15) & 1) != 0);
        attackInfo.setAttackAction(mask & 0x7FFF);
        decodeInt();
        attackInfo.setAttackActionType(decodeByte());
        final int skillID = attackInfo.skillId;
        if (skillID == 23111001 || skillID == 80001915 || skillID == 36111010) {
            decodeInt();
            decodeInt();
            decodeInt();
        }
        attackInfo.setAttackSpeed(decodeByte());
        attackInfo.setTick(decodeInt());
        attackInfo.setTotlalBulletCount(decodeInt());
        getClient().getCharacter().showDebugMessage("Shoot", ChatMsgType.SYSTEM, String.format("%d", (((long)attackInfo.tick) & 0xFFFFFFFFL)));

        decodeShort();
        decodeShort();
        attackInfo.setByteIdk1(decodeByte());

        boolean bySteal = false;
        if (!SkillConstants.isShootSkillNotConsumingBullet(skillID, bySteal)) {
            attackInfo.setBulletCount(decodeInt());
        }

        decodeShort();
        decodeShort();
        decodeShort();

        for (int i = 0; i < attackInfo.getMobCount(); i++) {
            MobAttackInfo mobAttackInfo = new MobAttackInfo();
            int mobObjectID = decodeInt();
            final Mob mob = getClient().getCharacter().getField().getMobByObjectId(mobObjectID);
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
            long[] damages = new long[attackInfo.getHits()];
            for (int j = 0; j < damages.length; j++) {
                damages[j] = decodeLong();
            }
            int mobUpDownYRange = decodeInt();
            decodeInt(); // crc
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
            skip(4);
            mobAttackInfo.setObjectID(mobObjectID);
            mobAttackInfo.setIdk1(idk1);
            mobAttackInfo.setIdk2(idk2);
            mobAttackInfo.setIdk3(idk3);
            mobAttackInfo.setIdk4(idk4);
            mobAttackInfo.setIdk5(idk5);
            mobAttackInfo.setTemplateID(templateID);
            mobAttackInfo.setCalcDamageStatIndex(calcDamageStatIndex);
            mobAttackInfo.setRcDstX(rcDstX);
            mobAttackInfo.setRectRight(rectRight);
            mobAttackInfo.setOldPosX(oldPosX);
            mobAttackInfo.setOldPosY(oldPosY);
            mobAttackInfo.setIdk6(idk6);
            mobAttackInfo.setDamages(damages);
            mobAttackInfo.setMobUpDownYRange(mobUpDownYRange);
            mobAttackInfo.setType(type);
            mobAttackInfo.setCurrentAnimationName(currentAnimationName);
            mobAttackInfo.setAnimationDeltaL(animationDeltaL);
            mobAttackInfo.setHitPartRunTimes(hitPartRunTimes);
            attackInfo.getMobAttackInfo().add(mobAttackInfo);
        }

    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final Skill skill = chr.getSkill(attackInfo.getSkillId());
        final SkillInfo si = SkillData.getInstance().getSkillInfoById(attackInfo.getSkillId());
        final boolean attackSuccess = chr.getJobHandler().handleAttack(attackInfo);
        final List<Mob> killedMob = new ArrayList<>();
        chr.getCharacterLocalStat().getCalcDamage().PDamageForPvM(attackInfo);
        field.broadcastPacket(new LP_UserShootAttack(chr, attackInfo));
        if (attackSuccess) {
            if (skill != null) {
                final int hpCon = si.getValue(SkillStat.hpCon, attackInfo.getSlv());
                final int mpCon = si.getValue(SkillStat.mpCon, attackInfo.getSlv());
                final int hpRCon = si.getValue(SkillStat.hpRCon, attackInfo.getSlv());
                if (hpCon > 0)
                    chr.addStat(Stat.HP, -hpCon);
                if (mpCon > 0)
                    chr.addStat(Stat.MP, -mpCon);
                if (hpRCon > 0)
                    chr.addStat(Stat.HP, (int) -(chr.getStat(Stat.HP) * (chr.getStat(Stat.HP) * (hpRCon / 100.0))));
            }
            chr.renewCharacterStats();
            chr.attackMob(attackInfo);
        } else {
            chr.showDebugMessage("攻擊失敗", ChatMsgType.SYSTEM,"物理攻擊失效");
        }
    }
}
