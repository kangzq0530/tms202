package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.SkillUseInfo;
import com.msemu.world.constants.SkillConstants;

/**
 * Created by Weber on 2018/5/19.
 */
public class CP_UserSkillUseRequest extends InPacket<GameClient> {

    private SkillUseInfo skillUseInfo = new SkillUseInfo();

    public CP_UserSkillUseRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        skillUseInfo.setUpdateTick(decodeInt());
        skillUseInfo.setSkillID(decodeInt());
        skillUseInfo.setSlv(decodeByte());

        if (SkillConstants.isZeroSkill(skillUseInfo.getSkillID())) {
            decodeByte();
            skillUseInfo.setZeroSkill(true);
        }

        skillUseInfo.setOption(decodeInt());

        if (((skillUseInfo.getOption() >> 4) & 1) > 0) {
            skillUseInfo.setPosition(decodePosition());
        }

        if( SkillConstants.isAntiRepeatBuffSkill(skillUseInfo.getSkillID())) {
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

        if(skillID == 100001261
                || skillID == 80001408
                || skillID == 25111206
                || skillID == 0xB8F3AD
                || skillID == 0x142482C
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
                || SkillConstants.is混沌共鳴(skillID) ) {
            decodeShort();
        } else {
            boolean isAffectedBitmap = decodeByte() > 0;

            switch (skillID) {
                case 2311001:
                case 112121010:
                    decodeShort();
                    decodeByte();
                    break;
                default:
                    break;
            }
            //

        }



    }

    @Override
    public void runImpl() {

    }
}
