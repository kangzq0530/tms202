package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class CreateGuildAgreeRequest implements IGuildResult {

    private String guildName;

    public CreateGuildAgreeRequest(String guildName) {
        this.guildName = guildName;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ReqCreateGuildAgree;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(guildName);
    }
}
