package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class PlayExclSoundWithDownBGMUserEffect implements IUserEffect {

    private String name;
    private int down;

    public PlayExclSoundWithDownBGMUserEffect(String name) {
        this.name = name;
        this.down = 0;
    }

    public PlayExclSoundWithDownBGMUserEffect(String name, int down) {
        this.name = name;
        this.down = down;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.PlayExclSoundWithDownBGM;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(name);
        outPacket.encodeInt(down);
    }
}
