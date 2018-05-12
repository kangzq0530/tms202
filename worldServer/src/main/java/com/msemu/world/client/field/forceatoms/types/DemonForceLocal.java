package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/12.
 */
public class DemonForceLocal  extends AbstractForceAtom {

    public DemonForceLocal(int count, int skillID, int createTime) {
        super(count, skillID, createTime);
        this.inc = 3;
        this.firstImpact = byMob ? Rand.get(0x20, 0x30) : Rand.get(0x0F, 0x20);
        this.secondImpact = byMob ? Rand.get(3, 4) : Rand.get(0x15, 0x30);
        this.angle = byMob ? Rand.get(0x0, 0xFF) : Rand.get(0x30, 0x50);
        this.startDelay = 540;
    }
}
