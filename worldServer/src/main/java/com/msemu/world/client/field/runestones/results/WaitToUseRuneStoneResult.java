package com.msemu.world.client.field.runestones.results;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.RuneResultType;

public class WaitToUseRuneStoneResult implements IRuneResult {

    private int period;

    public WaitToUseRuneStoneResult(int period) {
        this.period = period;
    }

    @Override
    public RuneResultType getType() {
        return RuneResultType.RRT_WAIT_TO_USE_RUNE;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(period);
    }
}
