package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

public class CP_AliveAck extends InPacket<GameClient> {

    public CP_AliveAck(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {
        getClient().removeIdleTask();
    }
}
