package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.GuildResultType;

public class SetMarkDoneResponse implements IGuildResult {

    private final Guild guild;

    public SetMarkDoneResponse(Guild guild) {
        this.guild = guild;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResSetMark_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(guild.getId());
        outPacket.encodeShort(guild.getMark());
        outPacket.encodeByte(guild.getMarkBgColor());
        outPacket.encodeShort(guild.getMark());
        outPacket.encodeByte(guild.getMarkColor());
    }
}
