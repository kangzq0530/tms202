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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.NpcMessageType;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_UserScriptMessageAnswer extends InPacket<GameClient> {

    private NpcMessageType lastMessageType;

    private byte action = -1;

    private int selection = -1;

    private String text;

    public CP_UserScriptMessageAnswer(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        lastMessageType = NpcMessageType.getByValue(decodeByte());

        if (NpcMessageType.NM_ASK_AVATAR_EX.equals(lastMessageType) && available() >= 4) {
            decodeShort();
        }

        if (available() > 0) {
            action = decodeByte();
        }

        if (NpcMessageType.NM_ASK_TEXT.equals(lastMessageType)) {
            if (action != 0) {
                text = decodeString();
            }
        } else if (available() >= 4) {
            selection = decodeInt();
        } else if (available() > 0) {
            selection = decodeByte();
        }


    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        chr.getScriptManager().handleAction(lastMessageType, action, selection);
    }
}
