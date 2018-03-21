package com.msemu.commons.network.handler;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.EClientState;
import com.msemu.commons.network.ReceivablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/3/14.
 */
class ClientPacketHandler<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(ClientPacketHandler.class);
    private final Map<EClientState, Map<Short, ReceivablePacket<TClient>>> packetsPrototypes = new HashMap<>();
    private final Map<Class<?>, Short> packetsOpcodes = new HashMap<>();

    ClientPacketHandler() {
    }

    private static void unknownPacket(EClientState state, short opcode) {
        log.error("[未知的封包] : OPCODE 0x{}, state={}", Integer.toHexString(opcode), state.toString());
    }

    void addPacketPrototype(ReceivablePacket<TClient> packetPrototype, EClientState... states) {
        for (EClientState state : states) {
            Map<Short, ReceivablePacket<TClient>> pm = this.packetsPrototypes.computeIfAbsent(state, (k) -> new HashMap());
            pm.put(packetPrototype.getOpcode(), packetPrototype);
        }
        if (!this.packetsOpcodes.containsKey(packetPrototype.getClass())) {
            this.packetsOpcodes.put(packetPrototype.getClass(), packetPrototype.getOpcode());
        }

    }

    private ReceivablePacket<TClient> getPacket(short id, TClient ch) {
        ReceivablePacket prototype = null;
        Map pm = (Map) this.packetsPrototypes.get(ch.getState());
        if (pm != null) {
            prototype = (ReceivablePacket) pm.get(id);
        }

        if (prototype == null) {
            unknownPacket(ch.getState(), id);
            return null;
        } else {
            return prototype.clonePacket();
        }
    }

    public short getOpcode(Class<?> packetClass) {
        return this.packetsOpcodes.get(packetClass);
    }

    public ReceivablePacket<TClient> handle(short id, TClient ch) {
        return this.getPacket(id, ch);
    }
}
