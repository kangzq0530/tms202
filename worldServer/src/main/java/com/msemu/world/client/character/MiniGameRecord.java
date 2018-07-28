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
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/14.
 */
public class MiniGameRecord {
    FileTime fileTime = new FileTime(0);
    private int sOwnerName; // string, yet 4 bytes?
    private int rewardGradeQ;
    private int rewardGradeQHead;
    private int rewardGradeQSize;

    public FileTime getFileTime() {
        return fileTime;
    }

    public void setFileTime(FileTime fileTime) {
        this.fileTime = fileTime;
    }

    public int getsOwnerName() {
        return sOwnerName;
    }

    public void setsOwnerName(int sOwnerName) {
        this.sOwnerName = sOwnerName;
    }

    public int getRewardGradeQ() {
        return rewardGradeQ;
    }

    public void setRewardGradeQ(int rewardGradeQ) {
        this.rewardGradeQ = rewardGradeQ;
    }

    public int getRewardGradeQHead() {
        return rewardGradeQHead;
    }

    public void setRewardGradeQHead(int rewardGradeQHead) {
        this.rewardGradeQHead = rewardGradeQHead;
    }

    public int getRewardGradeQSize() {
        return rewardGradeQSize;
    }

    public void setRewardGradeQSize(int rewardGradeQSize) {
        this.rewardGradeQSize = rewardGradeQSize;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getFileTime().getHighDateTime());
        outPacket.encodeInt(getsOwnerName());
        outPacket.encodeInt(getRewardGradeQ());
        outPacket.encodeInt(getRewardGradeQHead());
        outPacket.encodeInt(getRewardGradeQSize());
    }
}

