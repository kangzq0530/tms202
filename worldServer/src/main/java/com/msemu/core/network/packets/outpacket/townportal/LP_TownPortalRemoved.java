package com.msemu.core.network.packets.outpacket.townportal;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.party.TownPortal;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_TownPortalRemoved extends OutPacket<GameClient> {

    public LP_TownPortalRemoved(TownPortal portal, boolean fadeOut) {
        super(OutHeader.LP_TownPortalRemoved);
        encodeByte(fadeOut);
        encodeInt(portal.getObjectId());
    }
}
