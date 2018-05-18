package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestExpActData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestExpAction implements IQuestAction {
    @Getter
    @Setter
    private long exp;

    @Override
    public void action(Character chr) {
        chr.addExp(getExp());
    }

    @Override
    public void load(QuestActData actData) {
        if (actData instanceof QuestExpActData) {
            setExp(((QuestExpActData) actData).getExp());
        }
    }

}
