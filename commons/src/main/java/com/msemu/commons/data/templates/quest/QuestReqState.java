package com.msemu.commons.data.templates.quest;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestReqState {

    @Getter
    @Setter
    private int questId;

    @Getter
    @Setter
    private byte state;

    @Getter
    @Setter
    private int order;
}
