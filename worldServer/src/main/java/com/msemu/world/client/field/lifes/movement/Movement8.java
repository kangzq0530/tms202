package com.msemu.world.client.field.lifes.movement;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class Movement8 extends MovementBase {
    public Movement8(InPacket<GameClient> inPacket, byte command) {
        super();
        this.command = command;
        this.position = new Position(0, 0);

        this.stat = inPacket.decodeByte();
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getCommand());
        outPacket.encodeByte(getStat());
    }
}

