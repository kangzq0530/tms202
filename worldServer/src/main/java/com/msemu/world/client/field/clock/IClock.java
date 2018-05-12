package com.msemu.world.client.field.clock;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ClockType;

/**
 * Created by Weber on 2018/5/12.
 */
public interface IClock {

    ClockType getType();

    void encode(OutPacket<GameClient> outPacket);
}
