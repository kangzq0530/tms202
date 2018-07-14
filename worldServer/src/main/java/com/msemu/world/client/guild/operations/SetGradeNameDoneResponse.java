package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.GuildResultType;

public class SetGradeNameDoneResponse implements IGuildResult {

    private final Guild guild;

    public SetGradeNameDoneResponse(Guild guild) {
        this.guild = guild;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResSetGradeName_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(guild.getId());
        for(int i = 0 ; i < 5; i++)
            outPacket.encodeString(guild.getGradeNames().get(i));
    }
}
