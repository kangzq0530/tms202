package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/4.
 */
public class CP_UserBodyAttack extends InPacket<GameClient> {

    public CP_UserBodyAttack(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {

    }
}
