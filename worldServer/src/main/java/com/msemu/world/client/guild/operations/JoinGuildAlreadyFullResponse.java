package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class JoinGuildAlreadyFullResponse implements IGuildResult {
    @Override
    public GuildResultType getType() {
        return GuildResultType.ResJoinGuild_AlreadyFull;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        // 您要加入的公會人數已達上限！無法再加入該公會。
    }
}
