package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.LazyDatMappingDataLoader;
import com.msemu.commons.data.templates.field.FieldTemplate;

/**
 * Created by Weber on 2018/4/28.
 */
public class FieldTemplateDatLoader extends LazyDatMappingDataLoader<FieldTemplate> {
    public FieldTemplateDatLoader() {
        super(DatManager.Field);
    }

    @Override
    protected FieldTemplate create() {
        return new FieldTemplate();
    }
}
