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

package com.msemu.world.client.character.friends;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
@Setter
public class FriendEntry {

    private int charId;
    private String friendName;
    private byte flag;
    private int channelId;
    private String friendGroup;
    private int accountID;
    private boolean mobile;
    private String friendNick;
    private String friendMemo;
    private boolean inShop;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(charId);
        outPacket.encodeString(friendName, 15);
        outPacket.encodeByte(flag);
        outPacket.encodeInt(channelId);
        outPacket.encodeString(friendGroup, 18);
        outPacket.encodeInt(accountID);
        outPacket.encodeByte(mobile);
        outPacket.encodeString(friendNick);
        outPacket.encodeString(friendName, 271);
        outPacket.encodeInt(inShop ? 1 : 0);
    }

}
