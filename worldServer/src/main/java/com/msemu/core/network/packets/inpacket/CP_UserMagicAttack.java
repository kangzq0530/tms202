package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/4.
 */
public class CP_UserMagicAttack extends InPacket<GameClient> {

    public CP_UserMagicAttack(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {

    }
}
