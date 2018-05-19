package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_SetQuestClear extends OutPacket<GameClient> {

    public LP_SetQuestClear() {
        super(OutHeader.LP_SetQuestClear);
    }
}
