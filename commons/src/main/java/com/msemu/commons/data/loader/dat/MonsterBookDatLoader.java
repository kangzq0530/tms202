package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.ListDatDataLoader;
import com.msemu.commons.data.templates.MonsterBook;

/**
 * Created by Weber on 2018/5/6.
 */
public class MonsterBookDatLoader extends ListDatDataLoader<MonsterBook> {
    public MonsterBookDatLoader() {
        super(DatManager.MonsterBook);
    }

    @Override
    protected MonsterBook create() {
        return new MonsterBook();
    }
}
