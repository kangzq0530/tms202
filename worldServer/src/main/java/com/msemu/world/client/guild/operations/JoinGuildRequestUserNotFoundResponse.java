package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;

public class JoinGuildRequestUserNotFoundResponse implements IGuildResult {

    private final int characterID;

    public JoinGuildRequestUserNotFoundResponse(int characterID) {
        this.characterID = characterID;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResJoinGuild_NonRequestFindUser;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        // 搜尋不到邀請加入的角色.
        outPacket.encodeInt(characterID);
    }
}
