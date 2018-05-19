package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/4/19.
 */
public class LP_RecommendWorldMessag extends OutPacket<LoginClient> {
    public LP_RecommendWorldMessag(int world, String message) {
        super(OutHeader.LP_RecommendWorldMessage);
        if (message != null && !message.equals("")) {
            encodeInt(world);
            encodeByte(true);
        } else {
            encodeByte(false);
        }
    }
}
