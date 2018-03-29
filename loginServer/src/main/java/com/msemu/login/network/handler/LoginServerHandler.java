package com.msemu.login.network.handler;

import com.msemu.commons.network.InHeader;
import com.msemu.commons.network.InPacket;
import com.msemu.commons.network.netty.NettyClient;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.login.client.LoginClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginServerHandler extends MapleChannelHandler<LoginClient> {

    private static Logger log = LoggerFactory.getLogger(LoginServerHandler.class);

    @Override
    public void handlePacket(InHeader opcode, InPacket packet, LoginClient client) {
        super.handlePacket(opcode, packet, client);

        if(opcode.isNeedCheck()) {

        }

        switch (opcode) {
            case LoginBasicInfo:

        }
    }
}
