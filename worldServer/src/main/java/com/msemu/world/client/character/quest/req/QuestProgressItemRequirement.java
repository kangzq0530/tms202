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

import com.msemu.commons.data.templates.quest.reqs.QuestItemReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.database.Schema;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@DiscriminatorValue("item")
public class QuestProgressItemRequirement extends QuestProgressRequirement implements IQuestValueRequirement {


    @Column(name = "unitID")
    private int itemID;
    @Column(name = "requiredCount")
    private int requiredCount;
    @Column(name = "currentCount")
    private int currentCount;
    @Column(name =  "_order")
    private int order;

    public QuestProgressItemRequirement() {
    }

    public QuestProgressItemRequirement(int itemID, int requiredCount, int currentCount, int order) {
        this.itemID = itemID;
        this.requiredCount = requiredCount;
        this.currentCount = currentCount;
        this.order = order;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public void setRequiredCount(int requiredCount) {
        this.requiredCount = requiredCount;
    }

    public void incCurrentCount(int amount) {
        currentCount += amount;
        if (currentCount < 0) {
            currentCount = 0;
        }
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean isComplete() {
        return getCurrentCount() >= getRequiredCount();
    }

    @Override
    public String getValue() {
        return String.valueOf(getCurrentCount());
    }

    public void addItem(int quantity) {
        incCurrentCount(quantity);
    }

    @Override
    public void load(QuestReqData reqData) {
        setItemID(((QuestItemReqData)reqData).getItemId());
        setRequiredCount(((QuestItemReqData)reqData).getQuantity());
        setOrder(((QuestItemReqData)reqData).getOrder());
        setCurrentCount(0);
    }

    @Override
    public QuestProgressRequirement deepCopy() {
        return new QuestProgressItemRequirement(itemID, requiredCount, 0, order);
    }
}