package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.guild.operations.IGuildResultInfo;

/**
 * Created by Weber on 2018/4/14.
 */
public class LP_GuildResult extends OutPacket<GameClient> {

    public LP_GuildResult(IGuildResultInfo gri) {
        super(OutHeader.LP_GuildResult);
        gri.encode(this);
    }
}
