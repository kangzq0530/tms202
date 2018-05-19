package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/4/19.
 */
public class LP_SetPhysicalWorldID extends OutPacket<LoginClient> {

    public LP_SetPhysicalWorldID() {
        super(OutHeader.LP_SetPhysicalWorldID);
        encodeInt(0);
    }

}
