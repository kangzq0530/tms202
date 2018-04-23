package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestInfoNumberActData extends QuestActData {

    @Getter
    @Setter
    private int infoNumber;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.infoNumber;
    }
}
