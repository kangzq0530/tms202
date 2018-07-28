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

package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class MarriageRing {
    private int marriageNo;
    private int groomId;
    private int brideId;
    private int status;
    private int groomItemId;
    private int brideItemId;
    private String groomName;
    private String bridgeName;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getMarriageNo());
        outPacket.encodeInt(getGroomId());
        outPacket.encodeInt(getBrideId());
        outPacket.encodeShort(getStatus());
        outPacket.encodeInt(getGroomItemId());
        outPacket.encodeInt(getBrideItemId());
        outPacket.encodeString(getGroomName(), 15); //max length 13
        outPacket.encodeString(getBridgeName(), 15);
    }


    public void encodeForRemote(OutPacket outPacket) {
        // TODO make it so this works for a single player (groom/bride should be turned around 50% of the time)
        outPacket.encodeInt(getGroomId());
        outPacket.encodeInt(getBrideId());
        outPacket.encodeInt(getGroomItemId());
    }
}
