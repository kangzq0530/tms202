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

/**
 * Created by Weber on 2018/4/18.
 */
public class AbstractPacketHandlerFactory<TClient extends Client<TClient>> {
    private ClientPacketHandler<TClient> cHandler;


    AbstractPacketHandlerFactory(ClientPacketHandler<TClient> cph) {
        if (cph == null) {
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
