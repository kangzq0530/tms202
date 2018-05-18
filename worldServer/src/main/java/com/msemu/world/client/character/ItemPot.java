package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class ItemPot {

    private int lifeID;

    private byte level;

    private byte lastState;

    private int satiety;

    private int friendly;

    private int remainAbleFriendly;

    private int remainFriendlyTime;

    private byte maximumIncLevel;

    private int maximumIncSatiety;

    private int lastEatTime;

    private FileTime lastSleepStartTime;

    private FileTime lastDecSatietyTime;

    private FileTime consumedLastTime;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getLifeID());
        outPacket.encodeByte(getLevel());
        outPacket.encodeByte(getLastState());
        outPacket.encodeInt(getSatiety());
        outPacket.encodeInt(getFriendly());
        outPacket.encodeInt(getRemainAbleFriendly());
        outPacket.encodeInt(getRemainFriendlyTime());
        outPacket.encodeByte(getMaximumIncLevel());
        outPacket.encodeInt(getMaximumIncSatiety());
        outPacket.encodeFT(getLastEatTime());
        outPacket.encodeFT(getLastSleepStartTime());
        outPacket.encodeFT(getLastDecSatietyTime());
        outPacket.encodeFT(getConsumedLastTime());


    }
}

