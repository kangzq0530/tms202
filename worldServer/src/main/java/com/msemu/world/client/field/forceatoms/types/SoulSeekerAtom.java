package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/21.
 */
public class SoulSeekerAtom extends ForceAtomInfo {
    public SoulSeekerAtom() {
        super(1, Rand.get(15, 32), Rand.get(21, 48), Rand.get(48, 80), 540);
    }
}
