package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class CP_FuncKeyMappedModified extends InPacket<GameClient> {

    private int numChanged;


    public CP_FuncKeyMappedModified(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {

    }
}
