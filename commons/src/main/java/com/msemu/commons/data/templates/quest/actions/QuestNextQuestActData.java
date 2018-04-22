package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActionType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestNextQuestActData extends QuestActData {

    @Getter
    @Setter
    private int nextQuest;

    @Override
    public QuestActionType getType() {
        return QuestActionType.nextQuest;
    }
}
