package com.msemu.world.client.field.forceatoms.types;

import com.msemu.commons.utils.Rand;

/**
 * Created by Weber on 2018/5/21.
 */
public class SoulSeekerAtomRegen extends ForceAtomInfo {
    public SoulSeekerAtomRegen() {
        super(2, Rand.get(32, 48), Rand.get(3, 4), Rand.get(0, 255), 540);
    }
}
