package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_EnterTownPortalRequest extends InPacket<GameClient> {

    private int portalObjectID;


    public CP_EnterTownPortalRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        portalObjectID = decodeInt();
    }

    @Override
    public void runImpl() {

    }
}
