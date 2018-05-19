package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.lifes.movement.IMovement;

import java.util.List;

/**
 * Created by Weber on 2018/5/1.
 */
public class LP_UserMove extends OutPacket<GameClient> {
    public LP_UserMove(Character chr, int encodedGatherDuration, Position oldPos, Position oldVPos,
                       byte exceptionObject, List<IMovement> movements) {
        super(OutHeader.LP_UserMove);
        encodeInt(chr.getId());
        encodeInt(encodedGatherDuration);
        encodePosition(oldPos);
        encodePosition(oldVPos);
        encodeByte(movements.size());
        for(IMovement m : movements) {
            m.encode(this);
        }
    }
}
