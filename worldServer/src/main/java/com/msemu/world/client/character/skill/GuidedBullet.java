package com.msemu.world.client.character.skill;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
public class GuidedBullet extends TemporaryStatBase {

    @Getter
    @Setter
    private int mobID;
    @Getter
    @Setter
    private int userID;

    public GuidedBullet() {
        super(false);
        mobID = 0;
        userID = 0;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        super.encode(outPacket);
        outPacket.encodeInt(getMobID());
        outPacket.encodeInt(getUserID());
    }

    @Override
    public void reset() {
        super.reset();
        setMobID(0);
        setUserID(0);
    }
}
