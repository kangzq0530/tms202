package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_FieldUniverseMessage extends OutPacket<GameClient> {

    public LP_FieldUniverseMessage(int worldID, String fromName, String msg) {
        super(OutHeader.LP_FieldUniverseMessage);
        encodeByte(worldID);
        encodeString(fromName);
        encodeString(msg);
    }
}
