package com.msemu.world.constants;

import org.junit.Test;

/**
 * Created by Weber on 2018/5/20.
 */
public class GameConstantsTest {

    @Test
    public void testCalcBaseDamage() {

        int x = GameConstants.calcBaseDamage(344, 12, 0, 78, GameConstants.getJobDamageConst(112) + 1.49 , false);

        int y = (int) (1.2 * x + 0.5);
        int z = (int) (( x) / 5.0);

    }
}