package com.msemu.core.network.packets.outpacket.mob;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/13.
 */
public class LP_MobHpIndicator extends OutPacket<GameClient> {
    
    public LP_MobHpIndicator(int objectId, byte hpRate) {
        super(OutHeader.LP_MobHPIndicator);
        encodeInt(objectId);
        encodeByte(hpRate);
    }
}
