package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.enums.GuildResultType;

/**
 * Created by Weber on 2018/4/17.
 */
public class GuildUpdateMemberLogin implements IGuildResultInfo {

    private int guildID;
    private int charID;
    private boolean online;
    private boolean showBox;

    public GuildUpdateMemberLogin(int guildID, int charID, boolean online) {
        this.guildID = guildID;
        this.charID = charID;
        this.online = online;
        showBox = true;
    }

    public GuildUpdateMemberLogin(int guildID, int charID, boolean online, boolean showBox) {
        this.guildID = guildID;
        this.charID = charID;
        this.online = online;
        this.showBox = showBox;
    }

    @Override
    public GuildResultType getType() {
        return GuildResultType.ResNotifyLoginOrLogout;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(guildID);
        outPacket.encodeInt(charID);
        outPacket.encodeByte(online);
        outPacket.encodeByte(showBox);
    }
}