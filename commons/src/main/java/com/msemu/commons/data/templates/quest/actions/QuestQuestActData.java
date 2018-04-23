package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestQuestActData extends QuestActData {

    private int quest;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.quest;
    }
}
