package com.msemu.world.client.character.effect;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class ActionSetUsedUserEffect implements IUserEffect {

    private int type;
    private int eventID;
    private boolean upgrade, slv, byServer;

    public ActionSetUsedUserEffect(int type, int eventID, boolean upgrade, boolean slv, boolean byServer) {
        this.type = type;
        this.eventID = eventID;
        this.upgrade = upgrade;
        this.slv = slv;
        this.byServer = byServer;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.ActionSetUsed;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeShort(type);
        outPacket.encodeInt(eventID);
        outPacket.encodeByte(upgrade);
        outPacket.encodeByte(slv);
        outPacket.encodeByte(byServer);
    }
}
