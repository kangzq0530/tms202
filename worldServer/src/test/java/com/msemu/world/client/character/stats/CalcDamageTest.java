package com.msemu.world.client.character.stats;

import org.junit.Test;

/**
 * Created by Weber on 2018/5/21.
 */
public class CalcDamageTest {

    @Test
    public void testSeed() {
        CalcDamage calcDamage = new CalcDamage(null);

        calcDamage.setSeed(1, 1, 1);

    }
}