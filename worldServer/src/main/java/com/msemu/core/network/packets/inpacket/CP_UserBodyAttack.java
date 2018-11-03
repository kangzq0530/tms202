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
public class CP_UserBodyAttack extends InPacket<GameClient> {

    public CP_UserBodyAttack(short opcode) {
        super(opcode);
    }

    private AttackInfo attackInfo;

    @Override
    public void read() {
        attackInfo = new AttackInfo();

        /**
         * 01
         * 11
         * 30 73 40 01
         * 05
         * A9 76 EF AF
         * 00 00 00 00 00 00 00 00 00 00 00 00 00
         * 00
         * 00
         * 00 00
         * AA 9F 50 F1
         * 02
         * 00
         * 8D A7 7B 05
         * 00 00 00 00 E9 42 0F 00 07 00 00 8D 06 9D 75 2F 00 01 65 FD 8B 01 65 FD 8B 01 00 00 00 00 00 00 D2 1D 00 00 00 00 00 00 00 00 00 00 FD FF 72 93 00 01 34 FD 40 01 98 FD 8B 01 2E 23 EF 41 52 FD 8B 01
         */


        attackInfo.setFieldKey(decodeByte());
        Byte atkMask = decodeByte();
        attackInfo.setHits((byte) (atkMask & 0xF));
        attackInfo.setMobCount((atkMask & 0xF0) >> 4);
        attackInfo.setSkillId(decodeInt());
        attackInfo.setSlv(decodeByte());
        attackInfo.setCrc(decodeInt());
        final int nBossDmgR = decodeByte();
        skip(13);
        attackInfo.setSomeMask(decodeByte());
        Short actionMask = decodeShort();
        attackInfo.setLeft(((actionMask >> 15) & 1) != 0);
        attackInfo.setAttackAction((short) (actionMask & 0x7FFF));
        decodeInt(); // crc
        attackInfo.setAttackActionType(decodeByte());
        attackInfo.setAttackSpeed(decodeByte());
        attackInfo.setTick(decodeInt());
        attackInfo.setMobMove(decodeInt());

        for (int i = 0; i < attackInfo.mobCount; i++) {
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
            short oldPosX = decodeShort(); // ?
            short oldPosY = decodeShort(); // ?
            short idk6 = decodeShort();
            decodeInt();
            long[] damages = new long[attackInfo.hits];
            for (int j = 0; j < attackInfo.hits; j++) {
                damages[j] = decodeLong();
            }
            int mobUpDownYRange = decodeInt();
            decodeInt(); // crc
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
            skip(4);
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
            attackInfo.getMobAttackInfo().add(mai);
        }
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final int skillID = attackInfo.getSkillId();
        final int originSkill = SkillConstants.getLinkedSkill(skillID);
        final Skill skill = chr.getSkill(originSkill != 0 ? originSkill : skillID);
        final SkillInfo si = SkillData.getInstance().getSkillInfoById(attackInfo.getSkillId());
        final boolean attackSuccess = chr.getJobHandler().handleAttack(attackInfo);
        chr.getCharacterLocalStat().getCalcDamage().PDamageForPvM(attackInfo);
        field.broadcastPacket(new LP_UserMeleeAttack(chr, attackInfo));
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
