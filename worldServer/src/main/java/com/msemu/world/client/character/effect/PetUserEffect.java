package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class PetUserEffect implements IUserEffect {

    private int type;
    private int petIndex;

    public PetUserEffect(int type, int petIndex) {
        this.type = type;
        this.petIndex = petIndex;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.Pet;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(type);
        outPacket.encodeInt(petIndex);
    }
}
