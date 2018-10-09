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

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.miniroom.LP_MiniRoom;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.miniroom.actions.MiniRoomEnterAction;
import com.msemu.world.client.character.miniroom.actions.MiniRoomEnterResultAction;
import com.msemu.world.client.character.miniroom.actions.MiniRoomLeaveAction;
import com.msemu.world.client.field.AbstractFieldObject;
import com.msemu.world.enums.MiniRoomLeaveResult;
import com.msemu.world.enums.MiniRoomType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class MiniRoom extends AbstractFieldObject {


    @Getter
    protected final Map<Integer, MiniRoomVisitor> visitorsMap = new HashMap<>();

    @Getter
    protected final ReentrantLock lock = new ReentrantLock();

    public boolean closed = true;

    protected MiniRoom() {
    }

    public abstract MiniRoomType getType();

    public abstract int getMaxUsers();

    public abstract void create(Character creator);

    public abstract void enter(Character visitor);

    public abstract void leave(Character chr);

    public abstract void close();

    protected void broadcast(OutPacket<GameClient> outPacket) {
        broadcast(outPacket, null);
    }

    protected void broadcast(OutPacket<GameClient> outPacket, Character except) {
        useLock(() -> getVisitorsMap().forEach((index, visitor) -> {
            if (!visitor.getCharacter().equals(except)) {
                visitor.getCharacter().write(outPacket);
            }
        }));
    }

    public void useLock(Runnable runnable) {
        getLock().lock();
        try {
            runnable.run();
        } finally {
            getLock().unlock();
        }
    }

    public MiniRoomVisitor getVisitor(int index) {
        return getVisitorsMap().getOrDefault(index, null);
    }

    public List<MiniRoomVisitor> getVisitors() {
        return new ArrayList<>(this.visitorsMap.values());
    }

    protected void addVisitor(Character chr) {
        for (int i = 0; i < getMaxUsers(); i++) {
            if (!getVisitorsMap().containsKey(i)) {
                final MiniRoomVisitor visitor = new MiniRoomVisitor(i, chr);
                getVisitorsMap().put(i, visitor);
                chr.setMiniRoom(this);
                visitor.write(new LP_MiniRoom(new MiniRoomEnterResultAction(this, i)));
                broadcast(new LP_MiniRoom(new MiniRoomEnterAction(visitor)) , chr);
                return;
            }
        }

    }

    protected void removeVisitorByCharacter(Character chr) {
        for (int i = 0; i < getMaxUsers(); i++) {
            final MiniRoomVisitor visitor = getVisitorsMap().get(i);
            if (visitor.getCharacter() != null && visitor.getCharacter().equals(chr)) {
                getVisitorsMap().remove(i);
                return;
            }
        }
    }

    protected void removeVisitor(MiniRoomVisitor visitor) {
        if (getVisitorsMap().containsValue(visitor)) {
            broadcast(new LP_MiniRoom(new MiniRoomLeaveAction(MiniRoomLeaveResult.TR_TradeDone, visitor.getCharIndex(), visitor.getCharacter().getName())));
            getVisitorsMap().remove(visitor.getCharIndex());
            visitor.getCharacter().setMiniRoom(null);
        }
    }

    protected void removeAllVisitors() {
        getVisitors().forEach(this::removeVisitor);
    }

    public boolean isFull() {
        return getVisitorsMap().size() < getMaxUsers();
    }

    public boolean isClosed() {
        return closed;
    }

    protected void setClosed(boolean closed) {
        this.closed = closed;
    }

    public MiniRoomVisitor getVisitorByCharacter(Character chr) {
        return getVisitors().stream()
                .filter(each -> each.getCharacter().equals(chr)).findFirst()
                .orElse(null);
    }

}
