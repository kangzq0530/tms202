package com.msemu.world.network.packets.WvsContext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.world.client.character.skills.TemporaryStatManager;

/**
 * Created by Weber on 2018/4/13.
 */
public class TemporaryStatSet extends OutPacket {

    public TemporaryStatSet(TemporaryStatManager tsm) {
        super(OutHeader.TemporaryStatSet);
    }
}
