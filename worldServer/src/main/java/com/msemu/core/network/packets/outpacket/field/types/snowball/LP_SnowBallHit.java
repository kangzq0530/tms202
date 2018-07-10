package com.msemu.core.network.packets.outpacket.field.types.snowball;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/6/1.
 */
public class LP_SnowBallHit extends OutPacket<GameClient> {

    public LP_SnowBallHit(int target,  int damage, int delay) {
        super(OutHeader.LP_SnowBallHit);

        encodeByte(target);
        encodeShort(damage);
        encodeShort(delay);

    }
}
