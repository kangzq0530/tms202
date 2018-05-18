package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class TownPortal {

    private int portalIndex;

    private int townID = 0;

    private int fieldID = 0;

    private int skillID = 0;

    private Position position = new Position(0, 0);

    public TownPortal(int portalIndex) {
        this.portalIndex = 0;
    }

    public void encode(OutPacket<GameClient> outPacket, boolean isLeaving) {
        outPacket.encodeInt(isLeaving ? 999999999 : getTownID());
        outPacket.encodeInt(isLeaving ? 999999999 : getFieldID());
        outPacket.encodeInt(isLeaving ? 0 : getSkillID());
        outPacket.encodePosition(isLeaving ? new Position(-1, -1) : getPosition());
    }
}
