package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.GuildResultType;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/14.
 */
public class LoadGuildDoneResponse implements IGuildResult {
    @Getter
    public Guild guild;

    public LoadGuildDoneResponse(Guild guild) {
        this.guild = guild;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResLoadGuildDone;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(guild != null);
        outPacket.encodeByte(guild != null); // ??
        guild.encode(outPacket);
        outPacket.encodeInt(0); // aGuildNeedPoint
    }
}
