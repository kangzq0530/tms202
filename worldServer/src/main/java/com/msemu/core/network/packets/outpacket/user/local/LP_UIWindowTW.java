package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.utils.ISerializedFunction;

public class LP_UIWindowTW extends OutPacket<GameClient> {


    public LP_UIWindowTW(ISerializedFunction<OutPacket<GameClient>> function) {
        super(OutHeader.LP_UIWindowTW);
        encodeInt(function.getFunctionType().getValue());
        function.call(this);
    }

}
