package com.msemu.commons.network.handler;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.SendablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/3/15.
 */
class ServerPacketHandler<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(ServerPacketHandler.class);
    private final Map<SendablePacket<TClient>, Short> opcodes = new HashMap<>();

    ServerPacketHandler() {
    }

    short getOpCode(SendablePacket<TClient> packetClass) {
        Short opcode = this.opcodes.get(packetClass);
        if (opcode == null) {
            log.error("Can\'t find opcodes for opcodes: {}", packetClass.getClass().getSimpleName());
            return (short) 0;
        } else {
            return opcode;
        }
    }

    void addPacketOpcode(SendablePacket<TClient> packetClass, short opcode) {
        if (opcode >= 0) {
            if (this.opcodes.values().contains(opcode)) {
                throw new IllegalArgumentException(String.format("There already exists another opcodes with id 0x%02X", new Object[]{Short.valueOf(opcode)}));
            } else {
                this.opcodes.put(packetClass, opcode);
            }
        }
    }
}
