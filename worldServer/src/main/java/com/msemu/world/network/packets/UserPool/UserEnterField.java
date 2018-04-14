package com.msemu.world.network.packets.UserPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/12.
 */
public class UserEnterField extends OutPacket {

    public UserEnterField(Character character) {
        super(OutHeader.UserEnterField);
        //TODO: QQQ
    }
}
