package com.msemu.world.dataholders;

import com.msemu.core.configs.CoreConfig;
import junit.framework.TestCase;



/**
 * Created by Weber on 2018/3/21.
 */
public class PetDataTest extends TestCase {

    public void testPetData() {
        CoreConfig.WZ_PATH = "./wz";
        PetData.getInstance();
    }

}