package com.msemu.commons.network.handler;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.Client;
import com.msemu.commons.network.packets.InPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/18.
 */
public class ClientPacketHandler<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(ClientPacketHandler.class);
    private final Map<ClientState, Map<Short, InPacket<TClient>>> packetsPrototypes = new HashMap<>();
    private final Map<Class<? extends InPacket>, Short> packetsOpcodes = new HashMap<>();


    void addPacketPrototype(InPacket<TClient> packetPrototype, ClientState... states) {
        for (ClientState state : states) {
            Map<Short, InPacket<TClient>> pm = this.packetsPrototypes.computeIfAbsent(state, (k) -> new HashMap<>());
            pm.put(packetPrototype.getOpcode(), packetPrototype);
        }

        if(!this.packetsOpcodes.containsKey(packetPrototype.getClass())) {
            this.packetsOpcodes.put(packetPrototype.getClass(), packetPrototype.getOpcode());
        }
    }

    private InPacket<TClient> getPacket(short id, TClient ch) {
        InPacket<TClient> prototype = null;
        Map<Short, InPacket<TClient>> pm = this.packetsPrototypes.get(ch.getState());
        if(pm != null) {
            prototype = pm.get(id);
        }

        if(prototype == null) {
            unknownPacket(ch.getState(), id);
            return null;
        } else {
            return prototype.clonePacket();
        }
    }

    public short getOpcode(Class<? extends InPacket<TClient>> packetClass) {
        return this.packetsOpcodes.get(packetClass);
    }

    public InPacket<TClient> handle(short id, TClient ch) {
        return this.getPacket(id, ch);
    }

    private static void unknownPacket(ClientState state, short id) {
        //log.error("[UNKNOWN PACKET] : received 0x{}, state={}", Integer.toHexString(id), state.toString());
    }

    public void removeAllPrototypes() {
        this.packetsOpcodes.clear();
        this.packetsPrototypes.clear();
    }
}
