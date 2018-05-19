package com.msemu.core.network.packets.outpacket.user;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UIType;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_OpenUIWithOption extends OutPacket<GameClient> {

    public LP_OpenUIWithOption(UIType type , int npcID, int[] options) {
        super(OutHeader.LP_UserOpenUIWithOption);
        encodeInt(type.getValue());
        encodeInt(npcID);
        if(options == null || options.length == 0) {
            encodeInt(0);
        } else {
            Arrays.stream(options).forEach(this::encodeInt);
        }
    }
}
