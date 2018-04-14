package com.msemu.login.network.packets;

import com.msemu.commons.enums.InHeader;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OpcodeLoader;
import com.msemu.commons.network.netty.handler.MapleChannelHandler;
import com.msemu.core.configs.CoreConfig;
import com.msemu.login.client.LoginClient;
import com.msemu.login.network.packets.recv.LoginPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginServerHandler extends MapleChannelHandler<LoginClient> {

    private static Logger log = LoggerFactory.getLogger(LoginServerHandler.class);

    @Override
    public void handlePacket(InHeader opcode, InPacket packet, LoginClient client) {
        super.handlePacket(opcode, packet, client);

        if (opcode.isNeedCheck() && !client.isAuthorized()) {
            log.warn("Unauthorized Packet: " + packet);
            client.close();
            return;
        }

        if(CoreConfig.SHOW_PACKET) {
            OpcodeLoader.getInstance().reload();
        }

        LoginPacketHandler handler = LoginPacketHandler.getInstance();

        switch (opcode) {
            case ClientStart:
                handler.handleClientStart(client, packet);
            case LoginBasicInfo:
                handler.handleLoginBasicInfo(client, packet);
                break;
            case BackToLogin:
                handler.handleBackToLogin(client, packet);
            case LoginBackground:
                handler.handleLoginBackground(client, packet);
                break;
            case CheckLoginAuthInfo:
                handler.handleCheckLoginAuthInfo(client, packet);
                break;
            case WorldInfoRequest:
                handler.handleWorldInfoRequest(client, packet);
                break;
            case SelectWorld:
                handler.handleSelectWorld(client, packet);
                break;
            default:
                break;
        }

    }
}
