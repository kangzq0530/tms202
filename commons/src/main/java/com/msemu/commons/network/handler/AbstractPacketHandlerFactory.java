package com.msemu.commons.network.handler;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.Client;
import com.msemu.commons.network.packets.InPacket;

/**
 * Created by Weber on 2018/4/18.
 */
public class AbstractPacketHandlerFactory<TClient extends Client<TClient>> {
    private ClientPacketHandler<TClient> cHandler;


    AbstractPacketHandlerFactory(ClientPacketHandler<TClient> cph) {
        if(cph == null) {
            this.cHandler = new ClientPacketHandler<>();
        } else {
            this.cHandler = cph;
        }
    }

    void addClientPacket(InPacket<TClient> packetPrototype, ClientState... states) {
        this.cHandler.addPacketPrototype(packetPrototype, states);
    }

    public short getClientPacketOpCode(Class<? extends InPacket<TClient>> packetClass) {
        return this.cHandler.getOpcode(packetClass);
    }

    public InPacket<TClient> handleClientPacket(short id, TClient ch) {
        return this.cHandler.handle(id, ch);
    }


    public void removeAllPackets() {
        this.cHandler.removeAllPrototypes();
    }
}
