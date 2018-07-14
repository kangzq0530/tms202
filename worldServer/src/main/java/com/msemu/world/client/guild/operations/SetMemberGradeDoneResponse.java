package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.client.guild.GuildMember;
import com.msemu.world.enums.GuildResultType;

public class SetMemberGradeDoneResponse implements IGuildResult {

    private final Guild guild;
    private final GuildMember member;

    public SetMemberGradeDoneResponse(Guild guild, GuildMember member) {
        this.guild = guild;
        this.member = member;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResSetMemberGrade_Done;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(guild.getId());
        outPacket.encodeInt(member.getCharID());
        outPacket.encodeByte(member.getGrade());
    }
}
