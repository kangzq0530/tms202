package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.IntegerIndexedDatDataLoader;
import com.msemu.commons.data.templates.ItemOption;
import com.msemu.commons.data.templates.ItemOptionInfo;

/**
 * Created by Weber on 2018/4/28.
 */
public class ItemOptionDatLoader extends IntegerIndexedDatDataLoader<ItemOptionInfo> {
    public ItemOptionDatLoader() {
        super(DatManager.ItemOption);
    }

    @Override
    protected ItemOptionInfo create() {
        return new ItemOptionInfo();
    }
}
