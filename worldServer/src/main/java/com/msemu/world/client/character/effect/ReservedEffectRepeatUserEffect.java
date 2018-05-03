package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Weber on 2018/5/3.
 */
public class ReservedEffectRepeatUserEffect implements IUserEffect {

    private String itemName;



    @Override
    public UserEffectType getType() {
        return UserEffectType.ReservedEffectRepeat;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        throw new NotImplementedException();

    }
}
