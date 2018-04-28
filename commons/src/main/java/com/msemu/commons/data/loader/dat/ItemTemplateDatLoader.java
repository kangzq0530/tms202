package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.IntegerIndexedDatDataLoader;
import com.msemu.commons.data.templates.ItemTemplate;

/**
 * Created by Weber on 2018/4/28.
 */
public class ItemTemplateDatLoader extends IntegerIndexedDatDataLoader<ItemTemplate> {
    public ItemTemplateDatLoader() {
        super(DatManager.Item);
    }

    @Override
    protected ItemTemplate create() {
        return new ItemTemplate();
    }
}
