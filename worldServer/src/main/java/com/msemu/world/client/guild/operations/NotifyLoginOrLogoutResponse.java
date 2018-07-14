package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.GuildResultType;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/17.
 */
public class NotifyLoginOrLogoutResponse implements IGuildResult {

    @Getter
    private int guildID, charID;
    @Getter
    private boolean online, showBox;

    public NotifyLoginOrLogoutResponse(int guildID, int charID, boolean online) {
        this.guildID = guildID;
        this.charID = charID;
        this.online = online;
        this.showBox = true;
    }

    public NotifyLoginOrLogoutResponse(int guildID, int charID, boolean online, boolean showBox) {
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
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(guildID);
        outPacket.encodeInt(charID);
        outPacket.encodeByte(online);
        outPacket.encodeByte(showBox);
    }
}