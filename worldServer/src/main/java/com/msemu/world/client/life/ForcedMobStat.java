package com.msemu.world.client.life;

import com.msemu.commons.network.packets.OutPacket;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
@Setter
public class ForcedMobStat {
    private long maxHP, maxMP, exp;
    private int pad, mad, pdr, mdr, acc, eva, pushed, speed, level, userCount;

    public void encode(OutPacket outPacket) {
        outPacket.encodeLong(getMaxHP());
        outPacket.encodeInt((int) getMaxMP());
        outPacket.encodeInt((int) getExp());
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
        copy.setMaxMP((int) getMaxMP());
        copy.setExp((int) getExp());
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
