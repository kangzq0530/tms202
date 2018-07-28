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

package com.msemu.world.client.character.salon;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.BeautySalonOperationType;
import com.msemu.world.enums.SerializedFunctionType;

public class PreviewSalonResponse extends AbstractSalonResponse {

    @Override
    public SerializedFunctionType getFunctionType() {
        return SerializedFunctionType.PREVIEW_SALON_RESPONSE;
    }

    @Override
    public BeautySalonOperationType getType() {
        return BeautySalonOperationType.LOAD;
    }

    @Override
    public void call(OutPacket<GameClient> outPacket) {
        super.call(outPacket);
        outPacket.encodeInt(25631);
        outPacket.encodeByte(0);
        outPacket.encodeInt(3);
        outPacket.encodeInt(3);
        outPacket.encodeInt(3);
        outPacket.encodeZeroBytes(21);
        outPacket.encodeInt(3);
        outPacket.encodeZeroBytes(12);


    }
}
