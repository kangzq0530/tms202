package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.skill.LarknessManager;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_IncLarknessResponse extends OutPacket {
    public LP_IncLarknessResponse(LarknessManager larknessManager) {
        super(OutHeader.LP_ChangeLarknessStack);
    }
}
