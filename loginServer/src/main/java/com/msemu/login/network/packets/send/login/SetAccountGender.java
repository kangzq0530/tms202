package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.login.client.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class SetAccountGender extends OutPacket {

    public SetAccountGender(LoginClient client) {
        super(OutHeader.RequestSetGender);
        this.encodeString(client.getAccount().getUsername());
    }
}
