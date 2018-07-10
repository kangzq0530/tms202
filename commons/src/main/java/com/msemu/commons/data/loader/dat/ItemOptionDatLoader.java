package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatMappingDataLoader;
import com.msemu.commons.data.templates.ItemOptionInfo;

/**
 * Created by Weber on 2018/4/28.
 */
public class ItemOptionDatLoader extends DatMappingDataLoader<ItemOptionInfo> {
    public ItemOptionDatLoader() {
        super(DatManager.ItemOption);
    }

    @Override
    protected ItemOptionInfo create() {
        return new ItemOptionInfo();
    }
}
