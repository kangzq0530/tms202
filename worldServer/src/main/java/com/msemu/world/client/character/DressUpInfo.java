package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/14.
 */
@Getter
@Setter
public class DressUpInfo {

    private int face;
    private int hair;
    private int clothe;
    private byte skin;
    private int mixBaseHairColor = -1;
    private int mixAddHairColor;
    private int mixHairBaseProb;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getHair());
        outPacket.encodeInt(getClothe());
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getMixBaseHairColor());
        outPacket.encodeInt(getMixAddHairColor());
        outPacket.encodeInt(getMixHairBaseProb());
    }
}
