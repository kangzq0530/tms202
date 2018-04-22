package com.msemu.world.client.character.quest.req;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@DiscriminatorValue("maxLevel")
public class QuestProgressLevelRequirement extends QuestProgressRequirement {

    @Column(name = "requiredCount")
    private int level;
    @Column(name = "currentCount")
    private int curLevel;

    public QuestProgressLevelRequirement() {
    }

    public QuestProgressLevelRequirement(int level){
        this.level = level;
    }

    @Override
    public boolean isComplete() {
        return getCurLevel() >= getLevel();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

}

