package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatDataLoader;
import com.msemu.commons.data.templates.skill.VCoreInfo;

/**
 * Created by Weber on 2018/5/7.
 */
public class VCoreDatLoader extends DatDataLoader<VCoreInfo> {
    public VCoreDatLoader() {
        super(DatManager.VCoreInfo);
    }

    @Override
    protected VCoreInfo create() {
        return new VCoreInfo();
    }
}
