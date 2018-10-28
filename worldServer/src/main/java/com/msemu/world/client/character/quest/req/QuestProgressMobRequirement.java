/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestMobReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.database.Schema;
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

    public QuestProgressMobRequirement(int mobID, int requiredCount, int currentCount) {
        this.mobID = mobID;
        this.requiredCount = requiredCount;
        this.currentCount = currentCount;
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

    @Override
    public QuestProgressRequirement deepCopy() {
        return new QuestProgressMobRequirement(mobID, requiredCount, 0);
    }
}