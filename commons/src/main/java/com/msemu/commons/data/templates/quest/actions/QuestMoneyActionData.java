package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActionType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestMoneyActionData extends QuestActData {

    @Getter
    @Setter
    private long money;

    @Override
    public QuestActionType getType() {
        return QuestActionType.money;
    }
}
