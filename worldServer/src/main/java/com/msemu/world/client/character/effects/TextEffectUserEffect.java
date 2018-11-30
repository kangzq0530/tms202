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

package com.msemu.world.client.character.effects;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class TextEffectUserEffect implements IUserEffect {

    private String speech;
    private int textSpeed;
    private int showTime;
    private int origin;
    private int rx;
    private int ry;
    private int align;
    private int lineSpace;
    private int screenCoord;

    public TextEffectUserEffect(String speech, int textSpeed, int showTime, int origin, int rx, int ry, int align, int lineSpace, int screenCoord) {
        this.speech = speech;
        this.textSpeed = textSpeed;
        this.showTime = showTime;
        this.origin = origin;
        this.rx = rx;
        this.ry = ry;
        this.align = align;
        this.lineSpace = lineSpace;
        this.screenCoord = screenCoord;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.TextEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(speech);
        outPacket.encodeInt(textSpeed);
        outPacket.encodeInt(showTime);
        outPacket.encodeInt(origin);
        outPacket.encodeInt(rx);
        outPacket.encodeInt(ry);
        outPacket.encodeInt(lineSpace);
        outPacket.encodeInt(screenCoord);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
    }
}
