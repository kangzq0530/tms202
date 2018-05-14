package com.msemu.world.client.field.lifes.movement;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/1.
 */
public class Movement1 extends MovementBase {
    public Movement1(InPacket<GameClient> inPacket, byte command) {
        super();
        this.command = command;

        short x = inPacket.decodeShort();
        short y = inPacket.decodeShort();
        position = new Position(x, y);

        short xv = inPacket.decodeShort();
        short yv = inPacket.decodeShort();
        vPosition = new Position(xv, yv);

        fh = inPacket.decodeShort();

        if (command == 15 || command == 17) {
            footStart = inPacket.decodeShort();
        }



        short xoffset = inPacket.decodeShort();
        short yoffset = inPacket.decodeShort();
        offset = new Position(xoffset, yoffset);

        moveAction = inPacket.decodeByte();
        elapse = inPacket.decodeShort();
        forcedStop = inPacket.decodeByte();
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getCommand());
        outPacket.encodePosition(getPosition());
        outPacket.encodePosition(getVPosition());
        outPacket.encodeShort(getFh());
        if (getCommand() == 15 || getCommand() == 17) {
            outPacket.encodeShort(getFootStart());
        }
        outPacket.encodePosition(getOffset());
        outPacket.encodeByte(getMoveAction());
        outPacket.encodeShort(getDuration());
        outPacket.encodeByte(getForcedStop());
    }
}