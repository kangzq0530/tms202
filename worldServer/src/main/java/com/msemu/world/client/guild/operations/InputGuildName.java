package com.msemu.world.client.guild.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.wvscontext.GuildResult;
import com.msemu.world.enums.GuildResultType;

/**
 * Created by Weber on 2018/4/30.
 */
public class InputGuildName implements IGuildResultInfo {
    @Override
    public GuildResultType getType() {
        return GuildResultType.ReqInputGuildName;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

    }
}
