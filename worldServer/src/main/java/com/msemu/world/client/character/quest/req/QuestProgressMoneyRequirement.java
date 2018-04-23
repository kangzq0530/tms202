package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestEndMesoReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@DiscriminatorValue("money")
public class QuestProgressMoneyRequirement extends QuestProgressRequirement {

    @Column(name = "requiredCount")
    @Getter
    @Setter
    private int money;
    @Column(name = "currentCount")
    @Getter
    @Setter
    private int curMoney;

    @Override
    public boolean isComplete() {
        return getCurMoney() >= getMoney();
    }

    public void addMoney(int money) {
        setMoney(getMoney() + money);
    }

    @Override
    public void load(QuestReqData reqData) {
        if(reqData instanceof QuestEndMesoReqData) {
            setMoney(((QuestEndMesoReqData)reqData).getMeso());
        }
    }
}

