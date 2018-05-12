package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.types.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public abstract class AbstractForceAtom {
    protected boolean byMob, toMob;
    protected int count, inc, firstImpact, secondImpact, angle, startDelay, createTime,
            maxHitCount, effectIdx, arriveDir, arriveRange;
    protected int skillID;
    protected Position start;
    protected int num;
    public AbstractForceAtom(int count, int skillID, int createTime) {
        this.count = count;
        this.skillID = skillID;
        this.createTime = createTime;
    }
}
