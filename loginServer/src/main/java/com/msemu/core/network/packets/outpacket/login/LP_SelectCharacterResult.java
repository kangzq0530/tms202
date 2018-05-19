package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/22.
 */
public class LP_SelectCharacterResult extends OutPacket<LoginClient> {

    public LP_SelectCharacterResult(LoginResultCode resultType, byte[] ip, int port, int characterID) {
        super(OutHeader.LP_SelectCharacterResult);
        encodeByte(resultType.getValue());
        encodeByte(0);
        encodeArr(ip);
        encodeShort(port);
        encodeInt(0); // uChatIp
        encodeShort(0); // uChatPort
        encodeInt(characterID);
        encodeInt(characterID);
        encodeByte(0);
        encodeInt(0);
        encodeByte(0);
        encodeInt(0);

    }
}
