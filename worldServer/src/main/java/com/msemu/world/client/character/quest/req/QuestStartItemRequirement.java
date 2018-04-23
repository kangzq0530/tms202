package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestItemReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartItemRequirement implements IQuestStartRequirements {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int quantity;

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.hasItemCount(getId(), getQuantity());
    }

    @Override
    public void load(QuestReqData reqData) {
        if(reqData instanceof QuestItemReqData) {
            setId(((QuestItemReqData)reqData).getItemId());
            setQuantity(((QuestItemReqData)reqData).getQuantity());
        }
    }
}
