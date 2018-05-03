package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class PointUserEffect implements IUserEffect {

    private int colorType;
    private int index;

    public PointUserEffect(int colorType, int index) {
        this.colorType = colorType;
        this.index = index;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.Point;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(colorType);
        outPacket.encodeInt(index);
    }
}
