package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.operations.IGuildResult;

/**
 * Created by Weber on 2018/4/14.
 */
public class LP_GuildResult extends OutPacket<GameClient> {

    public LP_GuildResult(IGuildResult gri) {
        super(OutHeader.LP_GuildResult);
        encodeByte(gri.getType().getValue());
        gri.encode(this);
    }
}
