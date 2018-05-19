package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestQuestReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.QuestManager;

/**
 * Created by Weber on 2018/5/19.
 */
public class QuestStartQuestRequirement implements IQuestStartRequirements {

    private int quest;

    @Override
    public boolean hasRequirements(Character chr) {
        QuestManager qm = chr.getQuestManager();
        return qm.hasQuestCompleted(quest);
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestQuestReqData) {
            this.quest = ((QuestQuestReqData) reqData).getQuestId();
        }
    }
}
