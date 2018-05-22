package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.movement.IMovement;
import com.msemu.world.client.field.lifes.movement.MovementBase;

import java.util.List;

/**
 * Created by Weber on 2018/5/22.
 */
public class CP_AndroidMove extends InPacket<GameClient> {

    private int duation;
    private Position mPos;
    private Position oPos;
    private List<IMovement> movements;


    public CP_AndroidMove(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.duation = decodeInt();
        this.mPos = decodePosition();
        this.oPos = decodePosition();
        this.movements = MovementBase.decode(this);
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();

        if (movements.isEmpty())
            return;


        movements.forEach(move -> {
            // TODO move android
        });

    }
}
