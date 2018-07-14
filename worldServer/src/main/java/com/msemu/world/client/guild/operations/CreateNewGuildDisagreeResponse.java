package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class CreateNewGuildDisagreeResponse implements IGuildResult {
    @Override
    public GuildResultType getType() {
        return GuildResultType.ResCreateNewGuild_Disagree;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
