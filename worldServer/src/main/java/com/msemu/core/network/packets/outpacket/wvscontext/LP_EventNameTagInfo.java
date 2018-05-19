package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_EventNameTagInfo extends OutPacket<GameClient> {
    public LP_EventNameTagInfo() {
        super(OutHeader.LP_EventNameTagInfo);
        //TODO 這倒底啥封包
        for(int i = 0 ; i < 5; i++) {
            encodeString("");
            encodeByte(-1);
        }
    }
}
