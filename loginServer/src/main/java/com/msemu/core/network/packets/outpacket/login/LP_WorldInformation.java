package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_WorldInformation extends OutPacket<LoginClient> {

    public LP_WorldInformation(WorldInfo worldInfo) {
        super(OutHeader.LP_WorldInformation);

        encodeByte(worldInfo.getWorldId());
        encodeString(worldInfo.getName());
        encodeByte(worldInfo.getState());
        encodeString(worldInfo.getWorldEventDesc());
        encodeByte(worldInfo.getChannels().size());
        worldInfo.getChannels().forEach(channel -> {
            encodeString(channel.getName());
            encodeInt(channel.getChannelLoading());
            encodeByte(channel.getWorldId());
            encodeByte(channel.getChannel());
            encodeByte(0);
        });

        encodeShort(0);
        encodeInt(0);
        encodeByte(0);
    }
}
