package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.enums.FileTimeUnit;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public abstract class ForceAtomInfo {
    protected int count, inc, firstImpact, secondImpact, angle, startDelay,
            maxHitCount, effectIdx, arriveDir, arriveRange;
    protected Position start = new Position(0, 0);
    protected int num;
    protected FileTime createTime;

    public ForceAtomInfo(int inc, int firstImpact, int secondImpact, int angle, int startDelay) {
        this.inc = inc;
        this.firstImpact = firstImpact;
        this.secondImpact = secondImpact;
        this.angle = angle;
        this.startDelay = startDelay;
    }

    public boolean isExpired() {
        return getCreateTime().plus(5, FileTimeUnit.SEC).before(FileTime.now());
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(1);
        outPacket.encodeInt(this.getCount());
        outPacket.encodeInt(this.getInc());
        outPacket.encodeInt(this.getFirstImpact());
        outPacket.encodeInt(this.getSecondImpact());
        outPacket.encodeInt(this.getAngle());
        outPacket.encodeInt(this.getStartDelay());
        outPacket.encodeInt(this.getStart().getX());
        outPacket.encodeInt(this.getStart().getY());
        outPacket.encodeInt(this.getCreateTime().getLowDateTime());
        outPacket.encodeInt(this.getMaxHitCount());
        outPacket.encodeInt(this.getEffectIdx());
        outPacket.encodeInt(0);
    }
}
