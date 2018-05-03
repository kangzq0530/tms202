package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class AvatarOrientedMultipleRepeat implements IUserEffect {

    private String itemTypeName;
    private int x, y;

    public AvatarOrientedMultipleRepeat(String itemTypeName, int x, int y) {
        this.itemTypeName = itemTypeName;
        this.x = x;
        this.y = y;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.AvatarOrientedMultipleRepeat;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(itemTypeName);
        outPacket.encodeInt(x);
        outPacket.encodeInt(y);
    }
}
