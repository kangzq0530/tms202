package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class MarriageRing {
    private int marriageNo;
    private int groomId;
    private int brideId;
    private int status;
    private int groomItemId;
    private int brideItemId;
    private String groomName;
    private String bridgeName;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getMarriageNo());
        outPacket.encodeInt(getGroomId());
        outPacket.encodeInt(getBrideId());
        outPacket.encodeShort(getStatus());
        outPacket.encodeInt(getGroomItemId());
        outPacket.encodeInt(getBrideItemId());
        outPacket.encodeString(getGroomName(), 15); //max length 13
        outPacket.encodeString(getBridgeName(), 15);
    }


    public void encodeForRemote(OutPacket outPacket) {
        // TODO make it so this works for a single player (groom/bride should be turned around 50% of the time)
        outPacket.encodeInt(getGroomId());
        outPacket.encodeInt(getBrideId());
        outPacket.encodeInt(getGroomItemId());
    }
}
