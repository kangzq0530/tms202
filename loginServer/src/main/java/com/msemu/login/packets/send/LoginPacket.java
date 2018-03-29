package com.msemu.login.packets.send;

import com.msemu.commons.network.OutPacket;
import com.msemu.commons.network.netty.NettyClient;
import com.msemu.core.configs.CoreConfig;
import org.apache.logging.log4j.core.Core;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginPacket {

    public static OutPacket sendConnect(NettyClient client) {
        OutPacket outPacket = new OutPacket();
        outPacket.encodeShort((short) (0x0D + CoreConfig.GAME_PATCH_VERSION.length()));
        outPacket.encodeShort(CoreConfig.GAME_VERSION);
        outPacket.encodeString(CoreConfig.GAME_PATCH_VERSION);
        outPacket.encodeArr(client.getRecvIV());
        outPacket.encodeArr(client.getSendIV());
        outPacket.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        return outPacket;
    }

}
