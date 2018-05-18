package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.AswanSiegeAttackUserEffectType;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class AswanSiegeAttackUserEffect implements IUserEffect {

    private AswanSiegeAttackUserEffectType type;

    public AswanSiegeAttackUserEffect(AswanSiegeAttackUserEffectType type) {
        this.type = type;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.AswanSiegeAttack;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(type.getValue());
    }
}
