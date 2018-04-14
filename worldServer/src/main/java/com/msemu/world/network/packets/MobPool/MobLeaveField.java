package com.msemu.world.network.packets.MobPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/4/13.
 */
public class MobLeaveField extends OutPacket {

    public MobLeaveField(int objectID, byte deathType) {
        super(OutHeader.MobLeaveField);
        encodeInt(objectID);
        encodeByte(deathType);
    }
}
