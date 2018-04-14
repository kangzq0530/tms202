package com.msemu.world.client.character.skills;

/**
 * Created by Weber on 2018/4/11.
 */
public class TwoStateTemporaryStat extends TemporaryStatBase {

    public TwoStateTemporaryStat(boolean dynamicTermSet) {
        super(dynamicTermSet);
    }

    @Override
    public int getMaxValue() {
        return 0;
    }

    @Override
    public boolean isActive() {
        return getNOption() != 0;
    }

}