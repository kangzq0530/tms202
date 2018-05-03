package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class ConsumeUserEffect implements IUserEffect {

    private int itemID;

    public ConsumeUserEffect(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.ConsumeEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(itemID);
    }
}
