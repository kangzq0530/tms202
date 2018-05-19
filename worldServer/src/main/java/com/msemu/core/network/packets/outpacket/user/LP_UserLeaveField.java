package com.msemu.core.network.packets.outpacket.user;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_UserLeaveField extends OutPacket {

    public LP_UserLeaveField(Character character) {
        super(OutHeader.LP_UserLeaveField);
        encodeInt(character.getId());
    }
}
