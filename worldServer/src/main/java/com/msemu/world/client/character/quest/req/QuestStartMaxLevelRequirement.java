package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestLevelMaxReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMaxLevelRequirement implements IQuestStartRequirements {

    @Getter
    @Setter
    private short level;

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getLevel() <= getLevel();
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestLevelMaxReqData) {
            setLevel((short) ((QuestLevelMaxReqData) reqData).getMaxLevel());
        }
    }
}
