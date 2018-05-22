package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.TownPortal;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_TownPortal extends OutPacket<GameClient> {

    public LP_TownPortal(TownPortal townPortal, boolean leaving) {
        super(OutHeader.LP_TownPortal);

        encodeInt(townPortal.getObjectId());
        townPortal.encodeForTown(this, leaving);
    }
}
