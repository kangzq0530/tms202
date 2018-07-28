package com.msemu.world.client.field.runestones.results;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.RuneResultType;

public class UseRuneStoneUnderRequiredLevelResult implements IRuneResult {
    @Override
    public RuneResultType getType() {
        return RuneResultType.RRT_UNDER_REQUIRED_LEVEL;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
