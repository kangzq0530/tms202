package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestLevelMinReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.database.Schema;
import com.msemu.world.client.character.quest.Quest;
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
@DiscriminatorValue("level")
public class QuestProgressLevelRequirement extends QuestProgressRequirement {

    @Column(name = "requiredCount")
    @Getter
    @Setter
    private int level;
    @Column(name = "currentCount")
    @Getter
    @Setter
    private int curLevel;

    public QuestProgressLevelRequirement(){
        this.level = 0;
    }

    @Override
    public boolean isComplete() {
        return getCurLevel() >= getLevel();
    }

    @Override
    public void load(QuestReqData reqData) {
        if(reqData instanceof QuestLevelMinReqData) {
            setLevel(((QuestLevelMinReqData)reqData).getMinLevel());
        }
    }
}

