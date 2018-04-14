package com.msemu.world.network.packets.DropPool;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.world.client.life.Drop;

/**
 * Created by Weber on 2018/4/12.
 */
public class DropEnterField extends OutPacket {

    public DropEnterField(Drop drop, Position posFrom, Position posTo, int ownerID) {
        super(OutHeader.DropEnterField);

    }
}
