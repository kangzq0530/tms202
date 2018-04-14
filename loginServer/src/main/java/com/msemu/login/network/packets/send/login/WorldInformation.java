package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.rmi.model.WorldInfo;

/**
 * Created by Weber on 2018/3/30.
 */
public class WorldInformation extends OutPacket {

    public WorldInformation(WorldInfo worldInfo) {
        super(OutHeader.WorldInformation);

        encodeByte(worldInfo.getWorldId());
        encodeString(worldInfo.getName());
        encodeByte(worldInfo.getState());
        encodeString(worldInfo.getWorldEventDesc());
        encodeByte(worldInfo.getChannels().size());
        worldInfo.getChannels().forEach(channel -> {
            encodeString(channel.getName());
            encodeInt(channel.getChannelLoading());
            encodeByte(channel.getWorldId());
            encodeByte(channel.getChannel() - 1);
            encodeByte(0);
        });

        encodeShort(0);
        encodeInt(0);
        encodeByte(0);
    }
}
