package com.msemu.world.client.field.runestones.results;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.RuneResultType;

public interface IRuneResult {

    RuneResultType getType();

    void encode(OutPacket<GameClient> outPacket);
}
