package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/12.
 */
public class DemonForceLocal extends ForceAtomInfo {
    public DemonForceLocal() {
        super(3, Rand.get(32, 48), Rand.get(3, 4), Rand.get(0, 255), 540);
    }
}
