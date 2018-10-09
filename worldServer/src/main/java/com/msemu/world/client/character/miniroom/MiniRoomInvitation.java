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

package com.msemu.world.client.character.miniroom;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.character.Character;
import lombok.Getter;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

public class MiniRoomInvitation {

    private static final AtomicInteger partyIdGenerator = new AtomicInteger(1);

    @Getter
    private final int id;

    @Getter
    private final WeakReference<Character> inviterRef;

    @Getter
    private final WeakReference<Character> inviteeRef;

    @Getter
    private final FileTime createAt;

    public MiniRoomInvitation(Character inviter, Character invitee) {
        this.id = partyIdGenerator.incrementAndGet();
        this.inviterRef = new WeakReference<>(inviter);
        this.inviteeRef = new WeakReference<>(invitee);
        this.createAt = FileTime.now();
    }

    public Character getInviter() {
        return getInviterRef().get();
    }

    public Character getInvitee() {
        return getInviteeRef().get();
    }
}
