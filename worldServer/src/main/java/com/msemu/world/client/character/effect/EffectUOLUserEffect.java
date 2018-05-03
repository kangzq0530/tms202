package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class EffectUOLUserEffect implements IUserEffect {

    private String effect;
    private boolean flip;
    private int delay;// guess
    private int type;
    private int itemID;

    public EffectUOLUserEffect(String effect, boolean flip, int delay, int type, int itemID) {
        this.effect = effect;
        this.flip = flip;
        this.delay = delay;
        this.type = type;
        this.itemID = itemID;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.EffectUOL;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(effect);
        outPacket.encodeByte(flip);
        outPacket.encodeInt(delay);
        outPacket.encodeInt(type);
        if( type == 2) {
            outPacket.encodeInt(itemID);
        }
    }
}
