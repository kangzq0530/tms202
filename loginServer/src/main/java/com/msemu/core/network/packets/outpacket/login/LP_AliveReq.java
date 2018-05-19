package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/5/3.
 */
public class LP_AliveReq extends OutPacket<LoginClient> {

    public LP_AliveReq() {
        super(OutHeader.LP_AliveReq);
    }
}
