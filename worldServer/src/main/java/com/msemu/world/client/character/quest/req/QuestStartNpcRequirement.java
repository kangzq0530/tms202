package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestNpcReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/5/20.
 */
public class QuestStartNpcRequirement implements IQuestStartRequirements {

    private int npcID;

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getField().getAllNpc().stream()
                .anyMatch(npc -> npc.getTemplateId() == npcID);
    }

    @Override
    public void load(QuestReqData reqData) {

        if (reqData instanceof QuestNpcReqData) {
            this.npcID = ((QuestNpcReqData) reqData).getNpcId();
        }

    }
}
