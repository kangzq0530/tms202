package com.msemu.world.utils;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Weber on 2018/3/21.
 */
public class SkillEffectUtilsTest extends TestCase {
    @Test
    public void testEvalBuff() throws Exception {
        int result = StatEffectUtils.evalEffectValue("5+10*(x-1)%", "x", 101, 0);

        assertEquals(15, result);
    }

}