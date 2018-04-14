package com.msemu.world.network.packets.MobPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/13.
 */
public class MobHpIndicator extends OutPacket {
    
    public MobHpIndicator(int objectId, byte percDamage) {
        super(OutHeader.MobHPIndicator);
        encodeInt(objectId);
        encodeByte(percDamage);
    }
}
