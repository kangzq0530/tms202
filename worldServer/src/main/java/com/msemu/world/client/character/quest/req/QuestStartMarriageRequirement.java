package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMarriageRequirement implements IQuestStartRequirements {
    @Override
    public boolean hasRequirements(Character chr) {
        return chr.isMarried();
    }

    @Override
    public void load(QuestReqData reqData) {

    }
}
