package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class TextEffectUserEffect implements IUserEffect {

    private String speech;
    private int textSpeed;
    private int showTime;
    private int origin;
    private int rx;
    private int ry;
    private int align;
    private int lineSpace;
    private int screenCoord;

    public TextEffectUserEffect(String speech, int textSpeed, int showTime, int origin, int rx, int ry, int align, int lineSpace, int screenCoord) {
        this.speech = speech;
        this.textSpeed = textSpeed;
        this.showTime = showTime;
        this.origin = origin;
        this.rx = rx;
        this.ry = ry;
        this.align = align;
        this.lineSpace = lineSpace;
        this.screenCoord = screenCoord;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.TextEffect;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeString(speech);
        outPacket.encodeInt(textSpeed);
        outPacket.encodeInt(showTime);
        outPacket.encodeInt(origin);
        outPacket.encodeInt(rx);
        outPacket.encodeInt(ry);
        outPacket.encodeInt(lineSpace);
        outPacket.encodeInt(screenCoord);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
    }
}
