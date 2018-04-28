package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.IntegerIndexedDatDataLoader;
import com.msemu.commons.data.templates.EquipTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Weber on 2018/4/27.
 */
public class EquipTemplateDatLoader extends IntegerIndexedDatDataLoader<EquipTemplate> {
    public EquipTemplateDatLoader() {
        super(DatManager.Equip);
    }

    @Override
    protected EquipTemplate create() {
        return new EquipTemplate();
    }

}
