package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/14.
 */
public class LikePoint {
    private int point;
    private FileTime incTime = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    private int season;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getPoint());
        outPacket.encodeFT(getIncTime());
        outPacket.encodeInt(getSeason());
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public FileTime getIncTime() {
        return incTime;
    }

    public void setIncTime(FileTime incTime) {
        this.incTime = incTime;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }
}

