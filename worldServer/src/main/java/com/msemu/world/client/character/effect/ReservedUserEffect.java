package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class ReservedUserEffect implements IUserEffect {

    private boolean screenCoord;
    private int rx;
    private int ry;
    private String effect;

    public ReservedUserEffect(boolean screenCoord, int rx, int ry, String effect) {
        this.screenCoord = screenCoord;
        this.rx = rx;
        this.ry = ry;
        this.effect = effect;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.ReservedEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(screenCoord);
        outPacket.encodeInt(rx);
        outPacket.encodeInt(ry);
        outPacket.encodeString(effect);
    }
}
