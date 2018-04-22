package com.msemu.commons.data.templates.quest;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestItem {

    @Getter
    @Setter
    private int itemId;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private int order;
}
