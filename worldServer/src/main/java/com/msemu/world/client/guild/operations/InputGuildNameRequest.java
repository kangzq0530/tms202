package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

/**
 * Created by Weber on 2018/4/30.
 */
public class InputGuildNameRequest implements IGuildResult {
    @Override
    public GuildResultType getType() {
        return GuildResultType.ReqInputGuildName;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
