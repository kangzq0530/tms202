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

package com.msemu.world.service;

import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.miniroom.MiniRoomInvitation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@StartupComponent("Service")
public class MiniRoomService {

    private static final AtomicReference<MiniRoomService> instance = new AtomicReference<>();

    private final Map<Integer, MiniRoomInvitation> invitations = new ConcurrentHashMap<>();

    public static MiniRoomService getInstance() {
        MiniRoomService value = instance.get();
        if (value == null) {
            synchronized (MiniRoomService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new MiniRoomService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public MiniRoomInvitation createInvitation(Character inviter, Character invitee) {
        MiniRoomInvitation invitation = new MiniRoomInvitation(inviter, invitee);
        this.invitations.put(invitee.getId(), invitation);
        return invitation;
    }

    public boolean hasInvitation(Character invitee, int invitationSN) {
        MiniRoomInvitation invitation = this.invitations.getOrDefault(invitee.getId(), null);
        return invitation != null && invitation.getId() == invitationSN;
    }

    public boolean hasInvitation(Character invitee) {
        MiniRoomInvitation invitation = this.invitations.getOrDefault(invitee.getId(), null);
        return invitation != null;
    }

    public void removeInvitation(Character invitee) {
        if(hasInvitation(invitee)) {
            this.invitations.remove(invitee.getId());
        }
    }

    public MiniRoomInvitation getInvitationByInvitee(Character invitee) {
        return this.invitations.getOrDefault(invitee.getId(), null);
    }
}
