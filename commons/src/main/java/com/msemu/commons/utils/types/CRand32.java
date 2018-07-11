package com.msemu.commons.utils.types;

import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.Rand32;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/5/21.
 */
public class CRand32 {

    private long seed1;
    private long seed2;
    private long seed3;
    private long oldSeed1;
    private long oldSeed2;
    private long oldSeed3;

    public CRand32() {//constructor
        int randInt = Rand.nextInt();//idk, just make a random number to use as default seeds
        seed(randInt, randInt, randInt);
    }

    public long random() {
        long s1 = this.seed1;
        long s2 = this.seed2;
        long s3 = this.seed3;

        this.oldSeed1 = s1;
        this.oldSeed2 = s2;
        this.oldSeed3 = s3;

        int newSeed1 = (int) ((s1 << 12) ^ ((s1 >> 19)) ^ ((s1 >> 6) ^ ((s1 << 12))) & 0x1FFF);
        int newSeed2 = (int) (16 * s2 ^ ((s2 >> 25) & 0xFF) ^ ((16 * s2) ^ ((s2 >> 23) & 0xFF)) & 0x7F);
        int newSeed3 = (int) ((s3 >> 11) ^ (s3 << 17) ^ ((s3 >> 8) ^ (s3 << 17)) & 0x1FFFFF);
        this.seed1 = newSeed1 & 0xFFFFFFFFL;
        this.seed2 = newSeed2 & 0xFFFFFFFFL;
        this.seed3 = newSeed3 & 0xFFFFFFFFL;
        return (seed1 ^ seed2 ^ seed3) & 0xffffffffL;//& 0xffffffffl will help you convert long to unsigned int
    }

    public void seed(long s1, long s2, long s3) {
        this.seed1 = s1 | 0x100000L;
        this.oldSeed1 = s1 | 0x100000L;

        this.seed2 = s2 | 0x1000L;
        this.oldSeed2 = s2 | 0x1000L;

        this.seed3 = s3 | 0x10L;
        this.oldSeed3 = s3 | 0x10L;
    }
}
