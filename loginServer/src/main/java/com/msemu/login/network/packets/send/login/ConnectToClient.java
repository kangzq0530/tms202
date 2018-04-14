package com.msemu.login.network.packets.send.login;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.configs.CoreConfig;
import com.msemu.login.client.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class ConnectToClient extends OutPacket {
    public ConnectToClient(LoginClient client) {
        super();
        this.encodeShort(0x2E);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeString(CoreConfig.GAME_PATCH_VERSION);
        this.encodeArr(client.getRecvIV());
        this.encodeArr(client.getSendIV());
        this.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        this.encodeByte(0);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeShort(0);
        this.encodeArr(client.getRecvIV());
        this.encodeArr(client.getSendIV());
        this.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        String mPatchVer = CoreConfig.GAME_PATCH_VERSION.split(":")[0];
        this.encodeInt(Short.parseShort(mPatchVer));
        this.encodeInt(Short.parseShort(mPatchVer));
        this.encodeInt(0);
        this.encodeShort(1);
    }
}
