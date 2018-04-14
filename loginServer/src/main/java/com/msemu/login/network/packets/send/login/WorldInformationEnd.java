package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;

/**
 * Created by Weber on 2018/3/30.
 */
public class WorldInformationEnd extends OutPacket {

    public WorldInformationEnd() {
        super(OutHeader.WorldInformation);
        encodeByte(-1);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
    }
}
