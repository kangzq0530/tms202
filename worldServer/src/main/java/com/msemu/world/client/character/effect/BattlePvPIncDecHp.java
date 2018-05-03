package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class BattlePvPIncDecHp  implements IUserEffect {

    private int colorType;
    private String unk;

    public BattlePvPIncDecHp(int colorType, String unk) {
        this.colorType = colorType;
        this.unk = unk;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.BattlePvPIncDecHp;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(colorType);
        outPacket.encodeString(unk);
    }
}
