package com.msemu.core.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.ICipher;
import com.msemu.commons.network.impl.crypt.MapleAESOFB;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.sendable.LPHello;

/**
 * Created by Weber on 2018/3/15.
 */
public class LoginClient extends Client<LoginClient>
{
    public LoginClient(Connection<LoginClient> connection) {
        super(connection);
    }


    @Override
    protected void onOpen() {
        super.onOpen();
        final ICipher sendCipher = new MapleAESOFB(CoreConfig.GAME_VERSION);
        final ICipher recvCipher = new MapleAESOFB(CoreConfig.GAME_VERSION);
        this.getConnection().setSendCipher(sendCipher);
        this.getConnection().setRecvCipher(recvCipher);

        this.sendPacket(new LPHello(sendCipher, recvCipher));
    }
}
