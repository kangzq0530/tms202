package com.msemu.commons.network.handler;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.EClientState;
import com.msemu.commons.network.ReceivablePacket;
import com.msemu.commons.network.SendablePacket;

/**
 * Created by Weber on 2018/3/14.
 */
public class AbstractPacketHandlerFactory<TClient extends Client<TClient>> {
    private ClientPacketHandler<TClient> cHandler;
    private ServerPacketHandler<TClient> sHandler;

    AbstractPacketHandlerFactory(ServerPacketHandler<TClient> sph, ClientPacketHandler<TClient> cph) {
        if (sph == null) {
            this.sHandler = new ServerPacketHandler<>();
        } else {
            this.sHandler = sph;
        }

        if (cph == null) {
            this.cHandler = new ClientPacketHandler<>();
        } else {
            this.cHandler = cph;
        }

    }

    void addServerPacket(SendablePacket<TClient> packetClass, short opcode) {
        this.sHandler.addPacketOpcode(packetClass, opcode);
    }

    void addClientPacket(ReceivablePacket<TClient> packetPrototype, EClientState... states) {
        this.cHandler.addPacketPrototype(packetPrototype, states);
    }

    public short getServerPacketOpCode(SendablePacket<TClient> packetClass) {
        return this.sHandler.getOpCode(packetClass);
    }

    public short getClientPacketOpCode(Class<? extends ReceivablePacket> packetClass) {
        return this.cHandler.getOpcode(packetClass);
    }

    public ReceivablePacket<TClient> handleClientPacket(short id, TClient ch) {
        return this.cHandler.handle(id, ch);
    }
}
