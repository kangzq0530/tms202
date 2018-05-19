package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.client.character.Character;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/19.
 */
public class LP_CreateNewCharacterResult extends OutPacket<LoginClient> {

    public LP_CreateNewCharacterResult(LoginResultCode type, Character character) {
        super(OutHeader.LP_CreateNewCharacterResult);
        encodeByte(type.getValue());
        if(type == LoginResultCode.LoginSuccess) {
            character.getAvatarData().encode(this);
        }
    }
}
