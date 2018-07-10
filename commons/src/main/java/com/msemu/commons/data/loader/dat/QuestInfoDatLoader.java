package com.msemu.commons.data.loader.dat;

import com.msemu.commons.data.loader.DatMappingDataLoader;
import com.msemu.commons.data.templates.quest.QuestInfo;

/**
 * Created by Weber on 2018/4/28.
 */
public class QuestInfoDatLoader extends DatMappingDataLoader<QuestInfo> {
    public QuestInfoDatLoader() {
        super(DatManager.QuestInfo);
    }

    @Override
    protected QuestInfo create() {
        return new QuestInfo();
    }
}
