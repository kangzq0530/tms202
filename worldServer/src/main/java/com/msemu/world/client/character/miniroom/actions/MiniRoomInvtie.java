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
import com.msemu.world.client.character.miniroom.MiniRoomInvitation;
import com.msemu.world.enums.MiniRoomOperation;

public class MiniRoomInvtie implements IMiniRoomAction {

    private final MiniRoom miniRoom;

    private final Character inviter;

    private final MiniRoomInvitation invitation;

    public MiniRoomInvtie(MiniRoom miniRoom, Character inviter, MiniRoomInvitation invitation) {
        this.miniRoom = miniRoom;
        this.inviter = inviter;
        this.invitation = invitation;
    }

    @Override
    public MiniRoomOperation getType() {
        return MiniRoomOperation.MRP_Invite;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(miniRoom.getType().getValue());
        outPacket.encodeString(inviter.getName());
        outPacket.encodeInt(invitation.getId());
    }
}
