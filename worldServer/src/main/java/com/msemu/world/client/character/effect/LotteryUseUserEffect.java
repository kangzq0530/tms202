package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class LotteryUseUserEffect implements IUserEffect {

    private int itemID;
    private String effect;

    public LotteryUseUserEffect(int itemID) {
        this.itemID = itemID;
        this.effect = null;
    }

    public LotteryUseUserEffect(int itemID, String effect) {
        this.itemID = itemID;
        this.effect = effect;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.LotteryUse;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(itemID);
        outPacket.encodeByte(effect != null);
        if(effect != null)
            outPacket.encodeString(effect);
    }
}
