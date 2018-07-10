package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatDataLoader;
import com.msemu.commons.data.templates.ForbiddenName;

/**
 * Created by Weber on 2018/4/28.
 */
public class ForbiddenNameDatLoader extends DatDataLoader<ForbiddenName> {

    public ForbiddenNameDatLoader() {
        super(DatManager.ForbiddenName);
    }

    @Override
    protected ForbiddenName create() {
        return new ForbiddenName();
    }
}
