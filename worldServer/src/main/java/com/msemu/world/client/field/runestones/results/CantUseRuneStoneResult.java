package com.msemu.world.client.field.runestones.results;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.RuneResultType;

public class CantUseRuneStoneResult implements IRuneResult {
    @Override
    public RuneResultType getType() {
        return RuneResultType.RRT_CAN_NOT_USE;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
