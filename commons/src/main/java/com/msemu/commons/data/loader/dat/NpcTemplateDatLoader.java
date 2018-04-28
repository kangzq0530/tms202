package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.ListDatDataLoader;
import com.msemu.commons.data.loader.SetDatDataLoader;
import com.msemu.commons.data.templates.NpcTemplate;

/**
 * Created by Weber on 2018/4/28.
 */
public class NpcTemplateDatLoader extends SetDatDataLoader<NpcTemplate> {
    public NpcTemplateDatLoader() {
        super(DatManager.Npc);
    }

    @Override
    protected NpcTemplate create() {
        return new NpcTemplate();
    }
}
