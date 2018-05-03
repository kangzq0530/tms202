package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class IncubatorUseUserEffect implements IUserEffect {

    private int itemID;
    private String effect;

    public IncubatorUseUserEffect(int itemID, String effect) {
        this.itemID = itemID;
        this.effect = effect;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.IncubatorUse;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(itemID);
        outPacket.encodeString(effect);
    }
}
