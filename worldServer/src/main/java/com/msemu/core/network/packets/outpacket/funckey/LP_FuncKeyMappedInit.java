package com.msemu.core.network.packets.outpacket.funckey;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.FuncKeyMap;

/**
 * Created by Weber on 2018/4/25.
 */
public class LP_FuncKeyMappedInit extends OutPacket<GameClient> {

    public LP_FuncKeyMappedInit(FuncKeyMap funcKeyMap) {
        super(OutHeader.LP_FuncKeyMappedInit);
        funcKeyMap.encode(this);
    }

}
