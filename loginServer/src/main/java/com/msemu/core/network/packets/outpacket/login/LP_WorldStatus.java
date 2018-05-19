package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.enums.WorldStatus;

/**
 * Created by Weber on 2018/5/3.
 */
public class LP_WorldStatus extends OutPacket<LoginClient> {

    public LP_WorldStatus(WorldStatus status) {
        super(OutHeader.LP_WorldStatus);
        encodeByte(status.getValue());
    }

}
