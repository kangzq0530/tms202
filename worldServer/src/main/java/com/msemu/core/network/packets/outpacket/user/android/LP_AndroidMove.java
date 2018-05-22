package com.msemu.core.network.packets.outpacket.user.android;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_AndroidMove extends OutPacket<GameClient> {

    public LP_AndroidMove() {
        super(OutHeader.LP_AndroidMove);
    }
}
