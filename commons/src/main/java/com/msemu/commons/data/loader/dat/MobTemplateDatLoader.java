package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.IntegerIndexedDatDataLoader;
import com.msemu.commons.data.templates.MobTemplate;

/**
 * Created by Weber on 2018/4/28.
 */
public class MobTemplateDatLoader extends IntegerIndexedDatDataLoader<MobTemplate> {
    public MobTemplateDatLoader() {
        super(DatManager.Mob);
    }

    @Override
    protected MobTemplate create() {
        return new MobTemplate();
    }
}
