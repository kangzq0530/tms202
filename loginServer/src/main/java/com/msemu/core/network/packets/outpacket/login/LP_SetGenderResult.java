package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/4/19.
 */
public class LP_SetGenderResult extends OutPacket<LoginClient> {

    public LP_SetGenderResult(boolean result) {
        super(OutHeader.LP_SetGenderResult);
        encodeByte(result);
    }
}
