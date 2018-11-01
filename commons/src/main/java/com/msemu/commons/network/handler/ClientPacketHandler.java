/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.commons.network.handler;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.Client;
import com.msemu.commons.network.packets.InPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/18.
 */
public class ClientPacketHandler<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(ClientPacketHandler.class);
    private final Map<ClientState, Map<Short, InPacket<TClient>>> packetsPrototypes = new HashMap<>();
    private final Map<Class<? extends InPacket>, Short> packetsOpcodes = new HashMap<>();

    private static void unknownPacket(ClientState state, short id) {
        //log.error("[UNKNOWN PACKET] : received 0x{}, state={}", Integer.toHexString(id), state.toString());
    }

    void addPacketPrototype(InPacket<TClient> packetPrototype, ClientState... states) {
        for (ClientState state : states) {
            Map<Short, InPacket<TClient>> pm = this.packetsPrototypes.computeIfAbsent(state, (k) -> new HashMap<>());
            pm.put(packetPrototype.getOpcode(), packetPrototype);
        }

        if (!this.packetsOpcodes.containsKey(packetPrototype.getClass())) {
            this.packetsOpcodes.put(packetPrototype.getClass(), packetPrototype.getOpcode());
        }
    }

    @SuppressWarnings("all")
    private InPacket<TClient> getPacket(short id, TClient ch) {
        InPacket<TClient> prototype = null;
        Map<Short, InPacket<TClient>> pm = this.packetsPrototypes.get(ch.getState());
        if (pm != null) {
            prototype = pm.get(id);
        }
        if (prototype == null) {
            unknownPacket(ch.getState(), id);
            return null;
        } else {
            try {
                return (InPacket<TClient>) prototype.getClass().getDeclaredConstructor(new Class[]{Short.TYPE})
                        .newInstance(prototype.getOpcode());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public short getOpcode(Class<? extends InPacket<TClient>> packetClass) {
        return this.packetsOpcodes.get(packetClass);
    }

    public InPacket<TClient> handle(short id, TClient ch) {
        return this.getPacket(id, ch);
    }

    public void removeAllPrototypes() {
        this.packetsOpcodes.clear();
        this.packetsPrototypes.clear();
    }
}
