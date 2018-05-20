package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/21.
 */
public class FlyingSword4 extends ForceAtomInfo {
    public FlyingSword4() {
        super(4, Rand.get(15, 29), Rand.get(5, 6), Rand.get(35, 50), 540);
    }
}
