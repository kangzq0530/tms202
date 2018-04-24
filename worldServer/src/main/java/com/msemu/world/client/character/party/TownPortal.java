package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class TownPortal {

    @Getter
    @Setter
    private int townID;

    @Getter
    @Setter
    private int fieldID;

    @Getter
    @Setter
    private int skillID;

    @Getter
    @Setter
    private Position position;


    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getTownID());
        outPacket.encodeInt(getFieldID());
        outPacket.encodeInt(getSkillID());
        outPacket.encodePosition(getPosition());
    }
}
