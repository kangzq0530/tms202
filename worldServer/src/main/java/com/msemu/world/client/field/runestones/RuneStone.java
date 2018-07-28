package com.msemu.world.client.field.runestones;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.RuneStoneType;
import lombok.Getter;
import lombok.Setter;

public class RuneStone {

    @Getter
    private final RuneStoneType type;

    @Getter
    @Setter
    private boolean flip;

    @Getter
    @Setter
    private Position position = new Position(0, 0);

    @Getter
    private final int index;

    public RuneStone(final RuneStoneType type, final  int index) {
        this.type = type;
        this.index = index;
    }


    public void encode(OutPacket<GameClient> outPacket)
    {
        outPacket.encodeInt(getType().getValue());
        outPacket.encodeInt(getPosition().getX());
        outPacket.encodeInt(getPosition().getY());
        outPacket.encodeByte(isFlip());
    }

}
