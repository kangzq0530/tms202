package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.GuildResultType;

public class CreateNewGuildDoneResponse implements IGuildResult {

    private Guild guild;

    public CreateNewGuildDoneResponse(Guild guild) {
        this.guild = guild;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResCreateNewGuild_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        this.guild.encode(outPacket);
    }
}
