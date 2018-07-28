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
        if (reqData instanceof QuestEndMesoReqData) {
            setMoney(((QuestEndMesoReqData) reqData).getMeso());
        }
    }
}

