package com.msemu.core.network.packets.outpacket.user.android;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Android;

/**
 * Created by Weber on 2018/5/22.
 */
public class LP_AndroidActionSet extends OutPacket<GameClient> {

    public LP_AndroidActionSet(Android android, int actionSet, int randomKey) {
        super(OutHeader.LP_AndroidActionSet);
        encodeInt(android.getOwner().getId());
        encodeByte(actionSet);
        encodeByte(randomKey);
    }
}
