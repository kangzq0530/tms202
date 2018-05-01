package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.user.remote.UserRemoteMove;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.life.movement.IMovement;
import com.msemu.world.client.life.movement.MovementBase;

import java.util.List;

/**
 * Created by Weber on 2018/5/1.
 */
public class UserMove extends InPacket<GameClient> {
    private byte fieldKey;
    private int duration;
    private Position mPos, oPos;
    private List<IMovement> movements;

    public UserMove(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        fieldKey = decodeByte();
        decodeInt();
        decodeInt();
        decodeByte();
        duration = decodeInt();
        mPos = decodePosition();
        oPos = decodePosition();
        movements = MovementBase.decode(this);

    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        for (IMovement m : movements) {
            Position pos = m.getPosition();
            chr.setOldPosition(chr.getPosition());
            chr.setPosition(pos);
            chr.setMoveAction(m.getMoveAction());
            chr.setLeft(m.getMoveAction() % 2 == 1);
            chr.setFoothold(m.getFh());
        }
        chr.enableActions();
        chr.getField().checkCharInAffectedAreas(chr);
        chr.getField().broadcastPacket(new UserRemoteMove(chr, duration, mPos, oPos, (byte) 0, movements), chr);
    }
}
