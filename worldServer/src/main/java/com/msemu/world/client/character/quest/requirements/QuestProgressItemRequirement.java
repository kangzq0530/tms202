package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.dat.DatSerializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@DiscriminatorValue("item")
public class QuestProgressItemRequirement extends QuestProgressRequirement implements IQuestValueRequirement {


    @Column(name = "unitID")
    private int itemID;
    @Column(name = "requiredCount")
    private int requiredCount;
    @Column(name = "currentCount")
    private int currentCount;

    public QuestProgressItemRequirement() {
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
        if(currentCount < 0) {
            currentCount = 0;
        }
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    @Override
    public boolean isComplete() {
        return getCurrentCount() >= getRequiredCount();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getItemID());
        dos.writeInt(getRequiredCount());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        QuestProgressItemRequirement qpir = new QuestProgressItemRequirement();
        qpir.setItemID(dis.readInt());
        qpir.setRequiredCount(dis.readInt());
        return qpir;
    }

    @Override
    public String getValue() {
        return String.valueOf(getCurrentCount());
    }

    public void addItem(int quantity) {
        incCurrentCount(quantity);
    }
}