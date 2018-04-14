package com.msemu.world.network.packets.UserPool.UserLocal;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.skills.LarknessManager;

/**
 * Created by Weber on 2018/4/12.
 */
public class IncLarknessResponse extends OutPacket {
    public IncLarknessResponse(LarknessManager larknessManager) {
        super(OutHeader.ChangeLarknessStack);
    }
}
