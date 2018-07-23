package com.msemu.world.client.field.lifes;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneAppear;
import com.msemu.core.network.packets.outpacket.field.LP_RuneStoneDisappear;
import com.msemu.world.enums.FieldObjectType;
import com.msemu.world.enums.RuneStoneType;
import lombok.Getter;
import lombok.Setter;

public class RuneStone extends Life {

    @Getter
    private final RuneStoneType type;

    @Getter
    @Setter
    private boolean flip;

    @Getter
    private final int index;

    public RuneStone(final RuneStoneType type, final  int index) {
        this.type = type;
        this.index = index;
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.RUNE;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_RuneStoneAppear(this));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_RuneStoneDisappear(this, null, true));
    }

    public void encode(OutPacket<GameClient> outPacket)
    {
        outPacket.encodeInt(getType().getValue());
        outPacket.encodeInt(getPosition().getX());
        outPacket.encodeInt(getPosition().getY());
        outPacket.encodeByte(isFlip());
    }

}
