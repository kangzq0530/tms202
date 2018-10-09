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

package com.msemu.world.client.character.miniroom.actions;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.miniroom.MiniRoom;
import com.msemu.world.enums.MiniRoomEnterResult;
import com.msemu.world.enums.MiniRoomOperation;

public class MiniRoomEnterResultAction implements IMiniRoomAction {

    private final MiniRoom miniRoom;
    private final int charIndex;
    private final MiniRoomEnterResult result;
    private final String text;

    public MiniRoomEnterResultAction(MiniRoom miniRoom, int charIndex) {
        this.miniRoom = miniRoom;
        this.charIndex = charIndex;
        this.result = MiniRoomEnterResult.MR_Success;
        this.text = "";
    }

    public MiniRoomEnterResultAction(MiniRoomEnterResult result, String text) {
        this.miniRoom = null;
        this.charIndex = -1;
        this.result = result;
        this.text = text;
    }

    @Override
    public MiniRoomOperation getType() {
        return MiniRoomOperation.MRP_EnterResult;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        if (result == MiniRoomEnterResult.MR_Success && miniRoom != null) {
            outPacket.encodeByte(miniRoom.getType().getValue());
            outPacket.encodeByte(miniRoom.getMaxUsers());
            outPacket.encodeByte(charIndex);
            miniRoom.getVisitorsMap().forEach((index, visitor) -> {
                final Character chr = visitor.getCharacter();
                outPacket.encodeByte(index);
                chr.getAvatarData()
                        .getAvatarLook().encode(outPacket);
                outPacket.encodeString(chr.getName());
                outPacket.encodeShort(chr.getJob());
            });
            outPacket.encodeByte(-1);
            outPacket.encodeByte(-1);
        } else {
            outPacket.encodeByte(0);
            outPacket.encodeByte(result.getValue());
            outPacket.encodeString(text);
        }
    }
}
