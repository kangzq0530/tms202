package com.msemu.core.network.packets.outpacket.summon;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.field.lifes.Summon;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_SummonEnterField extends OutPacket<GameClient> {

    public LP_SummonEnterField(Summon summon) {
        super(OutHeader.LP_SummonedEnterField);
        encodeInt(summon.getCharID());
        encodeInt(summon.getObjectId());
        encodeInt(summon.getSkillID());
        encodeByte(summon.getCharLevel());
        encodeByte(summon.getSlv());
        // CSummoned::Init
        encodePosition(summon.getPosition());
        encodeByte(summon.getAction());
        encodeShort(summon.getCurFoothold());
        encodeByte(summon.getMoveAbility());
        encodeByte(summon.getAssistType());
        encodeByte(summon.getEnterType());
        encodeInt(summon.getObjectId());
        encodeByte(summon.isFlyMob());
        encodeByte(summon.isBeforeFirstAttack());
        encodeInt(summon.getTemplateId());
        encodeInt(summon.getBulletID());
        AvatarLook al = summon.getAvatarLook();
        encodeByte(al != null);
        if (al != null) {
            al.encode(this);
        }
        if (summon.getSkillID() == 35111002) { // Tesla Coil
            encodeByte(summon.getTeslaCoilState());
            summon.getTeslaCoilPositions().forEach(this::encodePosition);
        }
        if (summon.getSkillID() == 42111003) { // Kishin Shoukan
            for (Position pos : summon.getKishinPositions()) {
                encodePosition(pos);
            }
        }
        encodeByte(summon.isJaguarActive());
        encodeInt(summon.getSummonTerm());
        encodeByte(summon.isAttackActive());
    }
}
