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

