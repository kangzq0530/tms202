package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class LightnessUserEffect implements IUserEffect {

    private boolean unk;

    private int value;

    public LightnessUserEffect(int value) {
        this.unk = false;
        this.value = value;
    }

    public LightnessUserEffect(boolean unk, int value) {
        this.unk = unk;
        this.value = value;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.Lightness;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(unk);
        if(unk)
            outPacket.encodeInt(value);
    }
}
