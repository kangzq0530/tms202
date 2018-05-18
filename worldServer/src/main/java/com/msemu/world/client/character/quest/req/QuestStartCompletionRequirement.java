package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestQuestReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.QuestManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartCompletionRequirement implements IQuestStartRequirements {
    private static final Logger log = LoggerFactory.getLogger(QuestStartCompletionRequirement.class);

    @Getter
    @Setter
    private int questID;
    @Getter
    @Setter
    private byte questStatus;

    @Override
    public boolean hasRequirements(Character chr) {
        QuestManager qm = chr.getQuestManager();
        switch (getQuestStatus()) {
            case 0: // Not started
                return !qm.hasQuestInProgress(getQuestID()) && !qm.hasQuestCompleted(getQuestID());
            case 1: // In progress
                return qm.hasQuestInProgress(getQuestID());
            case 2: // Completed
                return qm.hasQuestCompleted(getQuestID());
            default:
                log.error(String.format("Unknown status %d.", getQuestStatus()));
                return true;
        }
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestQuestReqData) {
            setQuestID(((QuestQuestReqData) reqData).getQuestId());
            setQuestStatus(((QuestQuestReqData) reqData).getState());
        }
    }
}

