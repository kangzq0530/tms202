package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class LeftMonsterNumberUserEffect implements IUserEffect {

    private int count;

    public LeftMonsterNumberUserEffect(int count) {
        this.count = count;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.LeftMonsterNumber;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(count);
    }
}
