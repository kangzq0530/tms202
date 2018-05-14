package com.msemu.world.client.field.lifes.movement;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class Movement3 extends MovementBase {
    public Movement3(InPacket<GameClient> inPacket, byte command) {
        super();
        this.command = command;
        this.position = new Position(0, 0);

        short xv = inPacket.decodeShort();
        short xy = inPacket.decodeShort();
        vPosition = new Position(xv, xy);

        if (command == 21 || command == 22)
            footStart = inPacket.decodeShort();

        moveAction = inPacket.decodeByte();
        elapse = inPacket.decodeShort();
        forcedStop = inPacket.decodeByte();
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getCommand());
        outPacket.encodePosition(getVPosition());
        if (getCommand() == 21 || getCommand() == 22) {
            outPacket.encodeShort(getFootStart());
        }
        outPacket.encodeByte(getMoveAction());
        outPacket.encodeShort(getDuration());
        outPacket.encodeByte(getForcedStop());
    }
}
