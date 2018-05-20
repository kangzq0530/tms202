package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/21.
 */
public class FlyingSword1 extends ForceAtomInfo {
    public FlyingSword1() {
        super(1, Rand.get(15, 29), Rand.get(5, 6), Rand.get(35, 50), 540);
    }
}
