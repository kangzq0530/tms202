package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

/**
 * Created by Weber on 2018/4/14.
 */
public interface IGuildResultInfo {

    GuildResultType getType();

    void encode(OutPacket<GameClient> outPacket);
}

