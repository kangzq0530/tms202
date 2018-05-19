package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_MapTransferResult extends OutPacket<GameClient> {

    public LP_MapTransferResult(Character chr) {
        super(OutHeader.LP_MapTransferResult);
        throw new NotImplementedException();
    }
}
