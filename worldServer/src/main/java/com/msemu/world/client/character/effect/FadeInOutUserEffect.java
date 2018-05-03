package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class FadeInOutUserEffect implements IUserEffect {

    private int timeFadeIn;
    private int timeDelay;
    private int timeFadeOut;
    private int alpha;

    public FadeInOutUserEffect(int timeFadeIn, int timeDelay, int timeFadeOut, int alpha) {
        this.timeFadeIn = timeFadeIn;
        this.timeDelay = timeDelay;
        this.timeFadeOut = timeFadeOut;
        this.alpha = alpha;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.FadeInOut;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(timeFadeIn);
        outPacket.encodeInt(timeDelay);
        outPacket.encodeInt(timeFadeOut);
        outPacket.encodeInt(alpha);
    }
}
