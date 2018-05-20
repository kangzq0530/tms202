package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestMobReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.database.Schema;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Schema
@Entity
@DiscriminatorValue("mob")
public class QuestProgressMobRequirement extends QuestProgressRequirement implements IQuestValueRequirement {

    @Column(name = "unitID")
    @Getter
    @Setter
    private int mobID;
    @Column(name = "requiredCount")
    @Getter
    @Setter
    private int requiredCount;
    @Column(name = "currentCount")
    @Getter
    @Setter
    private int currentCount;

    public QuestProgressMobRequirement() {
        this.currentCount = 0;
    }

    public void incCurrentCount(int amount) {
        currentCount += amount;
        if (currentCount < 0) {
            currentCount = 0;
        }
    }

    @Override
    public boolean isComplete() {
        return getCurrentCount() >= getRequiredCount();
    }


    @Override
    public String getValue() {
        return String.valueOf(getCurrentCount());
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestMobReqData) {
            setRequiredCount(((QuestMobReqData) reqData).getCount());
            setMobID(((QuestMobReqData) reqData).getMobId());
        }
    }
}