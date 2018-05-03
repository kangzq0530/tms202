package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class RobbinsBombUserEffect implements IUserEffect {

    private int bombCount;
    private boolean numberOnly;

    public RobbinsBombUserEffect(int bombCount) {
        this.bombCount = bombCount;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.RobbinsBomb;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(bombCount);
        outPacket.encodeByte(numberOnly);
    }
}
