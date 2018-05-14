package com.msemu.world.client.field.lifes;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
@Setter
public class ForcedMobStat {
    private long maxHP;
    private int maxMP, exp;
    private int pad, mad, pdr, mdr, acc, eva, pushed, speed, level, userCount;

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeLong(getMaxHP());
        outPacket.encodeInt(getMaxMP());
        outPacket.encodeInt(getExp());
        outPacket.encodeInt(getPad());
        outPacket.encodeInt(getMad());
        outPacket.encodeInt(getPdr());
        outPacket.encodeInt(getMdr());
        outPacket.encodeInt(getAcc());
        outPacket.encodeInt(getEva());
        outPacket.encodeInt(getPushed());
        outPacket.encodeInt(getSpeed());
        outPacket.encodeInt(getLevel());
        outPacket.encodeInt(getUserCount());
    }


    public ForcedMobStat deepCopy() {
        ForcedMobStat copy = new ForcedMobStat();
        copy.setMaxHP((int) getMaxHP());
        copy.setMaxMP(getMaxMP());
        copy.setExp(getExp());
        copy.setPad(getPad());
        copy.setMad(getMad());
        copy.setPdr(getPdr());
        copy.setMdr(getMdr());
        copy.setAcc(getAcc());
        copy.setEva(getEva());
        copy.setPushed(getPushed());
        copy.setSpeed(getSpeed());
        copy.setLevel(getLevel());
        copy.setUserCount(getUserCount());
        return copy;
    }
}
