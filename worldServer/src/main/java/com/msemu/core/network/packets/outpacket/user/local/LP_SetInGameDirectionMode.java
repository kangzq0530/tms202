package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_SetInGameDirectionMode extends OutPacket<GameClient> {

    public LP_SetInGameDirectionMode(boolean enable) {
        this(enable, enable, false, !enable);
    }

    public LP_SetInGameDirectionMode(boolean enable1, boolean enable2, boolean enable3,
                                     boolean showUI) {
        super(OutHeader.LP_SetInGameDirectionMode);
        encodeByte(enable1);
        encodeByte(enable2);
        if(enable1) {
            encodeByte(enable3);
            encodeByte(showUI);
        } else {
            encodeByte(showUI);
        }
    }
}
