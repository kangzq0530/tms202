package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/21.
 */
public class PhantomDeathCard extends ForceAtomInfo {
    public PhantomDeathCard() {
        super(2, Rand.get(15, 29), Rand.get(5, 6), Rand.get(3, 12), 540);
    }
}
