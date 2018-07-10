package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatMappingDataLoader;
import com.msemu.commons.data.loader.LazyDatMappingDataLoader;
import com.msemu.commons.data.templates.EquipTemplate;

/**
 * Created by Weber on 2018/4/27.
 */
public class EquipTemplateDatLoader extends LazyDatMappingDataLoader<EquipTemplate> {

    public EquipTemplateDatLoader() {
        super(DatManager.Equip);
    }

    protected EquipTemplate create() {
        return new EquipTemplate();
    }

}
