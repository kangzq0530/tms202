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

package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SpeechBalloonUserEffect implements IUserEffect {

    private boolean normal;
    private int id;
    private int linkType;
    private String speech;
    private int time;
    private int magLevel;
    private int rx;
    private int ry;
    private int height;
    private int lineSpace;
    private int screenCoord;

    public SpeechBalloonUserEffect(boolean normal, int id, int linkType, String speech, int time, int magLevel, int rx, int ry, int height, int lineSpace, int screenCoord) {
        this.normal = normal;
        this.id = id;
        this.linkType = linkType;
        this.speech = speech;
        this.time = time;
        this.magLevel = magLevel;
        this.rx = rx;
        this.ry = ry;
        this.height = height;
        this.lineSpace = lineSpace;
        this.screenCoord = screenCoord;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SpeechBalloon;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(normal);
        outPacket.encodeInt(id);
        outPacket.encodeInt(linkType);
        outPacket.encodeString(speech);
        outPacket.encodeInt(time);
        outPacket.encodeInt(magLevel);
        outPacket.encodeInt(rx);
        outPacket.encodeInt(ry);
        outPacket.encodeInt(height);
        outPacket.encodeInt(lineSpace);
        outPacket.encodeInt(screenCoord);

    }
}
