package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class BossShieldCountUserEffect implements IUserEffect {

    private int itemID;
    private int count;

    public BossShieldCountUserEffect(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.BossShieldCount;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(itemID);
        outPacket.encodeInt(count);
    }
}
