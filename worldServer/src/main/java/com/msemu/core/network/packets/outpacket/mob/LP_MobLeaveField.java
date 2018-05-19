package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.enums.DeathType;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_MobLeaveField extends OutPacket<GameClient> {

    public LP_MobLeaveField(Mob mob, DeathType deathType) {
        super(OutHeader.LP_MobLeaveField);
        encodeInt(mob.getObjectId());
        encodeByte(deathType.getValue());
    }

    public LP_MobLeaveField(Mob mob) {
        super(OutHeader.LP_MobLeaveField);
        encodeInt(mob.getObjectId());
        encodeByte(mob.getDeathType().getValue());
    }
}
