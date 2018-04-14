package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.guild.operations.IGuildResultInfo;

/**
 * Created by Weber on 2018/4/14.
 */
public class GuildResult extends OutPacket {

    public GuildResult(IGuildResultInfo gri) {
        super(OutHeader.GuildResult);
        gri.encode(this);
    }
}
