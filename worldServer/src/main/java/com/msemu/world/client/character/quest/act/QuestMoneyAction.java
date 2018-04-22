package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestMoneyActionData;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestMoneyAction implements IQuestAction {
    private long money;

    public QuestMoneyAction(long money) {
        this.money = money;
    }

    public QuestMoneyAction() {

    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }


    @Override
    public void action(Character chr) {
        chr.addMoney(money);
    }

    @Override
    public void load(QuestActData actData) {
        if(actData instanceof QuestMoneyActionData) {
            setMoney(((QuestMoneyActionData)actData).getMoney());
        }
    }

}

