package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.SelectCharacterResult;
import com.msemu.login.enums.LoginResultType;

/**
 * Created by Weber on 2018/4/23.
 */
public class DirectGoToField extends InPacket<LoginClient> {
    int characterId;
    boolean offlineMode;

    public DirectGoToField(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        characterId = decodeInt();
        offlineMode = decodeByte() > 0;
    }

    @Override
    public void runImpl() {
        getClient().write(new SelectCharacterResult(LoginResultType.LoginSuccess, new byte[]{(byte)192, (byte)168, (byte)156, 1}, (short) 0, 0));
    }
}
