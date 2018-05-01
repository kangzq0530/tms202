package com.msemu.world.client.life.skills;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/12.
 */
public class BurnedInfo {
    @Getter
    @Setter
    private int characterId, skillId, damage, interval, end, dotAnimation, dotCount, superPos, attackDelay, dotTickIdx, dotTickDamR;
    @Getter
    @Setter
    private int startTime;
    @Getter
    @Setter
    private int lastUpdate;

    public BurnedInfo deepCopy() {
        BurnedInfo copy = new BurnedInfo();
        copy.setCharacterId(getCharacterId());
        copy.setSkillId(getSkillId());
        copy.setDamage(getDamage());
        copy.setInterval(getInterval());
        copy.setEnd(getEnd());
        copy.setDotAnimation(getDotAnimation());
        copy.setDotCount(getDotCount());
        copy.setSuperPos(getSuperPos());
        copy.setAttackDelay(getAttackDelay());
        copy.setDotTickIdx(getDotTickIdx());
        copy.setDotTickDamR(getDotTickDamR());
        copy.setLastUpdate(getLastUpdate());
        copy.setStartTime(getStartTime());
        return copy;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getCharacterId());
        outPacket.encodeInt(getSkillId());
        outPacket.encodeInt(getDamage());
        outPacket.encodeInt(getInterval());
        outPacket.encodeInt(getEnd());
        outPacket.encodeInt(getDotAnimation());
        outPacket.encodeInt(getDotCount());
        outPacket.encodeInt(getSuperPos());
        outPacket.encodeInt(getAttackDelay());
        outPacket.encodeInt(getDotTickIdx());
        outPacket.encodeInt(getDotTickDamR());
        outPacket.encodeInt(getLastUpdate());
        outPacket.encodeInt(getStartTime());
    }
}