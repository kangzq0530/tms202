package com.msemu.world.client.character.skills;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/11.
 */
public class GuidedBullet extends TemporaryStatBase {

    private int mobID;
    private int userID;

    public GuidedBullet() {
        super(false);
        mobID = 0;
        userID = 0;
    }

    public int getMobID() {
        return mobID;
    }

    public void setMobID(int mobID) {
        this.mobID = mobID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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
