package com.msemu.commons.data.templates.quest;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestReqSkill {

    @Getter
    @Setter
    private int skillId;

    @Getter
    @Setter
    private byte level;

    @Getter
    @Setter
    private String levelCondition;

    @Getter
    @Setter
    private boolean acquire;
}
