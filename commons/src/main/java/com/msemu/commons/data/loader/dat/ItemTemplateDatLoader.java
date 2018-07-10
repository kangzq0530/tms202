package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatMappingDataLoader;
import com.msemu.commons.data.loader.LazyDatMappingDataLoader;
import com.msemu.commons.data.templates.ItemTemplate;

/**
 * Created by Weber on 2018/4/28.
 */
public class ItemTemplateDatLoader extends LazyDatMappingDataLoader<ItemTemplate> {
    public ItemTemplateDatLoader() {
        super(DatManager.Item);
    }

    @Override
    protected ItemTemplate create() {
        return new ItemTemplate();
    }
}
