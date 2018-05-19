package com.msemu.core.network.packets.outpacket;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_ConnectToClient extends OutPacket<GameClient> {

    public LP_ConnectToClient(GameClient client) {
        super();
        this.encodeShort(0x0E);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeString(CoreConfig.GAME_PATCH_VERSION);
        this.encodeArr(client.getRiv());
        this.encodeArr(client.getSiv());
        this.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
    }

}
