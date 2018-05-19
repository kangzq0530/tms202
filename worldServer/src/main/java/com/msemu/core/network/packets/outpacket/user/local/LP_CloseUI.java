package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UIType;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_CloseUI extends OutPacket<GameClient> {

    public LP_CloseUI(UIType type) {
        super(OutHeader.LP_UserCloseUI);
        encodeInt(type.getValue());
    }
}
