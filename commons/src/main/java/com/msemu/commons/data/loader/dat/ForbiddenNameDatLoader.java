package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.SingleObjectDatLoader;
import com.msemu.commons.data.templates.ForbiddenName;

/**
 * Created by Weber on 2018/4/28.
 */
public class ForbiddenNameDatLoader extends SingleObjectDatLoader<ForbiddenName> {

    public ForbiddenNameDatLoader() {
        super(DatManager.ForbiddenName);
    }

    @Override
    protected ForbiddenName create() {
        return new ForbiddenName();
    }
}
