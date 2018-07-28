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

package com.msemu.world.client.field.forceatoms;

import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.forceatoms.types.ForceAtomInfo;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/5/12.
 */
public class ForceAtomManager {

    @Getter
    private final Lock lock;
    @Getter
    private final Field field;
    @Getter
    private List<ForceAtomInfo> forceAtomInfos;
    @Getter
    private AtomicInteger count = new AtomicInteger(1);

    public ForceAtomManager(Field field) {
        this.field = field;
        this.forceAtomInfos = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    public void reset() {
        this.forceAtomInfos.clear();
        this.count.set(1);
    }

    public void addForceAtom(ForceAtomInfo fi) {
        lock.lock();
        try {
            fi.setCount(count.getAndIncrement());
            fi.setCreateTime(FileTime.now());
            this.forceAtomInfos.add(fi);
        } finally {
            lock.unlock();
        }
    }

    public ForceAtomInfo getForceAtom(int count) {
        lock.lock();
        try {
            return getForceAtomInfos().stream()
                    .filter(fi -> fi.getCount() == count)
                    .findFirst().orElse(null);
        } finally {
            lock.unlock();
        }
    }

    public void removeExpire() {
        lock.lock();
        try {
            List<ForceAtomInfo> toRemove = getForceAtomInfos().stream()
                    .filter(ForceAtomInfo::isExpired)
                    .collect(Collectors.toList());
            toRemove.forEach(this::removeAtom);
        } finally {
            lock.unlock();
        }
    }

    public void removeAtom(int count) {
        lock.lock();
        try {
            ForceAtomInfo toRemove = getForceAtomInfos().stream()
                    .filter(fi -> fi.getCount() == count).findFirst().orElse(null);
            if (toRemove != null)
                this.forceAtomInfos.remove(toRemove);
        } finally {
            lock.unlock();
        }
    }

    public void removeAtom(ForceAtomInfo atom) {
        lock.lock();
        try {
            this.forceAtomInfos.remove(atom);
        } finally {
            lock.unlock();
        }
    }
}
