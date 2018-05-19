package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.AffectedArea;
import com.msemu.world.constants.SkillConstants;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_AffectedAreaCreated extends OutPacket<GameClient> {

    public LP_AffectedAreaCreated(AffectedArea aa) {
        super(OutHeader.LP_AffectedAreaCreated);

        encodeInt(aa.getObjectId());
        encodeByte(aa.getMobOrigin());
        encodeInt(aa.getCharID());
        encodeInt(aa.getSkillID());
        encodeByte(aa.getSlv());
        encodeShort(aa.getDelay());
        encodeRect(aa.getRect());
        encodeInt(aa.getElemAttr());
        encodeInt(aa.getElemAttr()); // ?
        encodePosition(aa.getPosition());
        encodeInt(aa.getForce());
        encodeInt(aa.getOption());
        encodeByte(aa.getOption() != 0);
        encodeInt(aa.getIdk()); // ?
        if(SkillConstants.isFlipAffectAreaSkill(aa.getSkillID())) {
            encodeByte(aa.isFlip());
        }
        encodeInt(0); // ?

    }
}
