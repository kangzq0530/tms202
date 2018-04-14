package com.msemu.world.network.packets.CField;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.life.AffectedArea;
import com.msemu.world.constants.SkillConstants;

/**
 * Created by Weber on 2018/4/13.
 */
public class AffectedAreaCreated extends OutPacket {

    public AffectedAreaCreated(AffectedArea aa) {
        super(OutHeader.AffectedAreaCreated);

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
