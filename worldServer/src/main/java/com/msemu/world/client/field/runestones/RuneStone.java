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

package com.msemu.world.client.field.runestones;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.RuneStoneType;
import lombok.Getter;
import lombok.Setter;

public class RuneStone {

    @Getter
    private final RuneStoneType type;
    @Getter
    private final int index;
    @Getter
    @Setter
    private boolean flip;
    @Getter
    @Setter
    private Position position = new Position(0, 0);
    @Getter
    @Setter
    private boolean enable;

    public RuneStone(final RuneStoneType type, final int index) {
        this.type = type;
        this.index = index;
        this.enable = false;
    }


    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getType().getValue());
        outPacket.encodeInt(getPosition().getX());
        outPacket.encodeInt(getPosition().getY());
        outPacket.encodeByte(isFlip());
    }

}
