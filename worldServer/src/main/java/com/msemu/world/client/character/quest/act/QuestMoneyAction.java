package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestMoneyActData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestMoneyAction implements IQuestAction {
    @Getter
    @Setter
    private long money;

    @Override
    public void action(Character chr) {
        chr.addMoney(money);
    }

    @Override
    public void load(QuestActData actData) {
        if (actData instanceof QuestMoneyActData) {
            setMoney(((QuestMoneyActData) actData).getMoney());
        }
    }

}

