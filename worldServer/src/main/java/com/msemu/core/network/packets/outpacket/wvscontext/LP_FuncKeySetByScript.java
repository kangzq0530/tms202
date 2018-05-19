package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class LP_FuncKeySetByScript extends OutPacket<GameClient> {

    public LP_FuncKeySetByScript(int skillID, int keyIdx) {
        super(OutHeader.LP_FuncKeySetByScript);
        encodeByte(keyIdx != 0);
        encodeInt(skillID);
        if(keyIdx != 0)
            encodeInt(keyIdx);
    }

}
