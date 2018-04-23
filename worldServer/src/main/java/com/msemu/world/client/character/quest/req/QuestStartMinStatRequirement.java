package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestLevelMinReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestPopReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.act.QuestPopAction;
import com.msemu.world.enums.Stat;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMinStatRequirement implements IQuestStartRequirements {

    @Getter
    @Setter
    private Stat stat;

    @Getter
    @Setter
    private short reqAmount;


    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getStat(getStat()) >= getReqAmount();
    }

    @Override
    public void load(QuestReqData reqData) {
        switch (reqData.getType()) {
            case charismaMin:
                setStat(Stat.CHARISMA);
                // TODO
            case craftMin:
                setStat(Stat.CRAFT);
                // TODO
                break;
            case lvmin:
                setStat(Stat.LEVEL);
                setReqAmount((short) ((QuestLevelMinReqData)reqData).getMinLevel());
                break;
            case insightMin:
                setStat(Stat.INSIGHT);
                // TODO
                break;
            case senseMin:
                setStat(Stat.SENSE);
                // TODO
                break;
            case charmMin:
                setStat(Stat.CHARM);
                // TODO
                break;
            case pop:
                setStat(Stat.POP);
                setReqAmount((short) ((QuestPopReqData)reqData).getPop());
                break;
        }
    }
}
