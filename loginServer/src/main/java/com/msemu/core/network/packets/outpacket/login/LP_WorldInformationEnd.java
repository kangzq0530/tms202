package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_WorldInformationEnd extends OutPacket<LoginClient> {

    public LP_WorldInformationEnd() {
        super(OutHeader.LP_WorldInformation);
        encodeByte(-1);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
    }
}
