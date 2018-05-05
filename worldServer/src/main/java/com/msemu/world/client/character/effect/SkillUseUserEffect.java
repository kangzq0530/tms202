package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillUseUserEffect implements IUserEffect {

    private int skillID;

    public SkillUseUserEffect(int skillID) {
        this.skillID = skillID;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillUse;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        if (skillID == 22170074)
            outPacket.encodeByte(0);
        else if (skillID == 1320016)
            outPacket.encodeByte(0);
        else if (skillID == 4331006) {
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
        } else {
            if (skillID == 3211010
                    || skillID == 3111010
                    || skillID == 1100012) {
                outPacket.encodeByte(0); // nRet
                outPacket.encodeInt(0); // nNameHeight
                outPacket.encodeInt(0); // bFlip
                return;
            }
            if (skillID == 35001006) {
                return;
            }
            if (skillID == 91001020
                    || skillID == 91001021
                    || skillID == 91001017
                    || skillID == 91001018) {
                return;
            }
            if (skillID == 33111007) {
                return;
            }
            if (skillID == 30001062) {
                outPacket.encodeByte(false); // bLightnessOn
                outPacket.encodeShort(0); // pCurrentItemSlot.baseclass_0.baseclass_0.dummy[0]
                outPacket.encodeShort(0); // pCurrentItemSlot.p
                return;
            }
            if (skillID == 30001064) {
                outPacket.encodeByte(0);
                return;
            }
            if (skillID == 60001218 || skillID == 60011218) {
                outPacket.encodeInt(0); // nRet
                outPacket.encodeInt(0); // m_ptRopeConnectDest.x
                outPacket.encodeInt(0); // m_ptRopeConnectDest.y
                return;
            }
            if (skillID == 20041222
                    || skillID == 15001021
                    || skillID == 20051284) {
                outPacket.encodeInt(0); // m_ptBlinkLightOrigin.x
                outPacket.encodeInt(0); // m_ptBlinkLightOrigin.y
                outPacket.encodeInt(0); // m_ptBlinkLightDest.x
                outPacket.encodeInt(0); // m_ptBlinkLightDest.y
                return;
            }
        }
        if (SkillConstants.isSuperNovaSkill(skillID)) {
            outPacket.encodeInt(0); // sMsg.x
            outPacket.encodeInt(0); // sMsg.y
            return;
        }
        if (skillID == 80001851) {
            return;
        }
        if (skillID != 12001027 && skillID != 12001028) {
            if (skillID == 142121008) {
                return;
            }
            if (SkillConstants.isRwMultiChargeSkill(skillID)) {
                outPacket.encodeInt(0);
                return;
            }
            if (SkillConstants.isUnregisterdSkill(skillID)) {
                outPacket.encodeByte(0);
                return;
            }
            if (skillID < 101100100 || skillID > 101100101) {
            } else {
                if (SkillConstants.isMatchSkill(false, skillID)) {
                    return;
                }
            }
        }
    }
}
