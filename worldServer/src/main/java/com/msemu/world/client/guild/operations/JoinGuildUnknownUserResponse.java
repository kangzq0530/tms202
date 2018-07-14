package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class JoinGuildUnknownUserResponse implements IGuildResult{


    @Override
    public GuildResultType getType() {
        return GuildResultType.ResJoinGuild_UnknownUser;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        // 在目前所在的頻道找不到該角色。
    }
}
