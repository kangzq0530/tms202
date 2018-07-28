package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;

import java.util.List;

public class LP_UserRandAreaAttackRequest extends OutPacket<GameClient> {

    public LP_UserRandAreaAttackRequest(List<Mob> mobs, int skillId) {
        super(OutHeader.LP_UserRandAreaAttackRequest);
        encodeInt(skillId);
        encodeInt(mobs.size());
        mobs.forEach(mob -> {
            encodeInt(mob.getPosition().getX());
            encodeInt(mob.getPosition().getY());
            encodeInt(mob.getObjectId());
        });
    }
}
