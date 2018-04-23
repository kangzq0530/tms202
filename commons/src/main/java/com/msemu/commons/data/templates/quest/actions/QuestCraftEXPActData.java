package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestCraftEXPActData extends QuestActData {

    @Getter
    @Setter
    private int craftEXP;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.craftEXP;
    }
}
