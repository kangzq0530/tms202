package com.msemu.world.client.field;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/14.
 */
@Getter
@Setter
public class FieldCustom {
    private int partyBonusExpRate;
    private String bgm;
    private int bgFieldID;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getPartyBonusExpRate());
        outPacket.encodeString(getBgm());
        outPacket.encodeInt(getBgFieldID());
    }
}
