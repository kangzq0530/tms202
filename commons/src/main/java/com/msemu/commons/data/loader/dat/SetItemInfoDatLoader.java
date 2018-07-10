package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatMappingDataLoader;
import com.msemu.commons.data.templates.SetItemInfo;

/**
 * Created by Weber on 2018/5/6.
 */
public class SetItemInfoDatLoader extends DatMappingDataLoader<SetItemInfo> {

    public SetItemInfoDatLoader() {
        super(DatManager.SetItem);
    }

    @Override
    protected SetItemInfo create() {
        return new SetItemInfo();
    }
}
