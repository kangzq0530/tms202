package com.msemu.commons.utils.types;

import org.junit.Test;

/**
 * Created by Weber on 2018/5/21.
 */
public class CRand32Test {

    @Test
    public void testSeed() {
        CRand32 rand32 = new CRand32();
        rand32.seed(1, 1, 1);
        long[] nRand = new long[11];
        for(int i = 0 ; i < 11; i++)
            nRand[i] = rand32.random();
    }

}