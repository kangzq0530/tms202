package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class AvatarOrientedUserEffect implements IUserEffect {

    private String path;
    private boolean unk;

    public AvatarOrientedUserEffect(String path) {
        this(path, true);
    }

    public AvatarOrientedUserEffect(String path, boolean unk) {
        this.path = path;
        this.unk = unk;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.AvatarOriented;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(path);
        outPacket.encodeByte(unk);
    }
}
