package com.msemu.world.model.items.pet.templactes;

import com.msemu.commons.data.provider.interfaces.MapleData;

/**
 * Created by Weber on 2018/3/21.
 */
public class PetCommandT {

    private final int index;
    private final int prob;
    private final int inc;

    public PetCommandT(MapleData source) {
        index = Integer.parseInt(source.getName());
        inc = (Integer)source.getChildByPath("inc").getData();
        prob = (Integer)source.getChildByPath("prob").getData();
    }

    public int getIndex() {
        return index;
    }

    public int getProb() {
        return prob;
    }

    public int getInc() {
        return inc;
    }
}
