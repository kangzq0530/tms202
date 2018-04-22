package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestPopActionData;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestPopAction implements IQuestAction {

    private int pop;

    public QuestPopAction(int pop) {
        this.pop = pop;
    }

    public QuestPopAction() {

    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    @Override
    public void action(Character chr) {
        chr.addStat(Stat.POP, getPop());
    }

    @Override
    public void load(QuestActData actData) {
        if(actData instanceof QuestPopActionData) {
            setPop(((QuestPopActionData)actData).getPop());
        }
    }

}
