package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_SetAccountGender extends OutPacket<LoginClient> {

    public LP_SetAccountGender(LoginClient client) {
        super(OutHeader.LP_RequestSetGender);
        encodeString(client.getAccount().getUsername());
    }
}
