package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/14.
 */
@Getter
@Setter
public class LikePoint {
    private int point;
    private FileTime incTime = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);
    private int season;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getPoint());
        outPacket.encodeFT(getIncTime());
        outPacket.encodeInt(getSeason());
    }
}

