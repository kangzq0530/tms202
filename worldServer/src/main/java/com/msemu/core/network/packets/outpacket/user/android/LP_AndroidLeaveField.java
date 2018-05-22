package com.msemu.core.network.packets.outpacket.user.android;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Android;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_AndroidLeaveField extends OutPacket<GameClient> {

    public LP_AndroidLeaveField(Android android) {
        super(OutHeader.LP_AndroidLeaveField);
        encodeInt(android.getObjectId());
    }
}
