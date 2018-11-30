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
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.SkillUseInfo;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/19.
 */
public class CP_UserSkillUseRequest extends InPacket<GameClient> {

    private SkillUseInfo skillUseInfo;

    public CP_UserSkillUseRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        skillUseInfo = new SkillUseInfo();
        skillUseInfo.setUpdateTick(decodeInt());
        skillUseInfo.setSkillID(decodeInt());


        if (SkillConstants.isZeroSkill(skillUseInfo.getSkillID())) {
            decodeByte();
            skillUseInfo.setZeroSkill(true);
        }

        skillUseInfo.setSlv(decodeByte());
        skillUseInfo.setOption(decodeInt());

        if (((skillUseInfo.getOption() >> 4) & 1) > 0) {
            skillUseInfo.setPosition(decodePosition());
        }

        if (SkillConstants.isAntiRepeatBuffSkill(skillUseInfo.getSkillID())) {
            skillUseInfo.setPosition2(decodePosition());
        }

        switch (skillUseInfo.getSkillID()) {
            case 4111009:
            case 14111007:
            case 5201008:
            case 14111025:
                skillUseInfo.setBulletConsumeItemID(decodeInt());
                break;
            default:
                break;
        }

        final int skillID = skillUseInfo.getSkillID();

        if (skillID == 100001261
                || skillID == 80001408
                || skillID == 25111206
                || skillID == 12121005
                || skillID == 21121068
                || skillID == 37110002
                || skillID == 80001839
                || skillID == 80001840
                || skillID == 25111012
                || skillID == 25121055
                || skillID == 400020051
                || skillID == 400021039
                || skillID == 33121016
                || skillID == 33111013
                || skillID == 131001107
                || skillID == 131001207
                || skillID == 51120057
                || skillID == 400031012
                || skillID == 400001017
                || SkillConstants.is混沌共鳴(skillID)) {
            decodeShort();
        } else {
//            boolean isAffectedBitmap = decodeByte() > 0;
//
//            switch (skillID) {
//                case 2311001:
//                case 112121010:
//                    decodeShort();
//                    decodeByte();
//                    break;
//                default:
//                    break;
//            }
//            //

        }

        // if ( dwAffectedMemberBitmap ) 不知怎判斷= =

        boolean dwAffectedMemberBitmap = false;

        if (dwAffectedMemberBitmap) {
            decodeByte();
            if (skillID == 2311001 || skillID == 112121010) {
                decodeByte();
                decodeShort();
            }
        }
        int available = available();
        int shouldAvailable = 3;
        switch (skillID) {
            case 400001017:
                shouldAvailable += 4;
                break;
            case 33111013:
            case 33121016:
            case 131001107:
            case 131001207:
            case 51120057:
            case 37110002:
            case 400021039:
            case 25111012:
            case 25121055:
            case 400020051:
                shouldAvailable += 1;
                break;
            case 400020046:
                shouldAvailable += 1;
                break;
        }


        if (available != shouldAvailable) {
            int mobCount = decodeByte();
            for (int i = 0; i < mobCount; i++)
                skillUseInfo.getMobs().add(decodeInt());
        }

        decodeShort();
        decodeByte();

        switch (skillID) {
            case 400001017:
                decodeInt();
                break;
            case 33111013:
            case 33121016:
            case 131001107:
            case 131001207:
            case 51120057:
            case 37110002:
            case 400021039:
            case 25111012:
            case 25121055:
            case 400020051:
                decodeByte();
                break;
            case 400020046:
                decodeByte();
                break;
        }


    }

    @Override
    public void runImpl() {

        Character chr = getClient().getCharacter();


        boolean result = chr.getJobHandler().handleSkillUse(skillUseInfo);

        if (result) {
            final Skill skill = chr.getSkill(skillUseInfo.getSkillID());
            final SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());
            final int hpCon = si.getValue(SkillStat.hpCon, skillUseInfo.getSlv());
            final int mpCon = si.getValue(SkillStat.mpCon, skillUseInfo.getSlv());
            final int hpRCon = si.getValue(SkillStat.hpRCon, skillUseInfo.getSlv());
            if (hpCon > 0)
                chr.addStat(Stat.HP, -hpCon);
            if (mpCon > 0)
                chr.addStat(Stat.MP, -mpCon);
            if (hpRCon > 0)
                chr.addStat(Stat.HP, (int) (chr.getStat(Stat.HP) * (chr.getStat(Stat.HP) * (hpRCon / 100.0))));
            chr.renewCharacterStats();
        }

    }
}
