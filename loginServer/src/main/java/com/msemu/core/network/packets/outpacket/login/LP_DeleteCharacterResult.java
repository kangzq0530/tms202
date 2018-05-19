package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/22.
 */
public class LP_DeleteCharacterResult extends OutPacket<LoginClient> {

    public LP_DeleteCharacterResult(int charId, LoginResultCode result) {
        super(OutHeader.LP_DeleteCharacterResult);

        encodeInt(charId);
        encodeByte(result.getValue());
    }
}
