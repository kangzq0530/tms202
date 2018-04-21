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
@DiscriminatorValue("mob")
public class QuestProgressMobRequirement extends QuestProgressRequirement implements IQuestValueRequirement {

    @Column(name = "unitID")
    private int mobID;
    @Column(name = "requiredCount")
    private int requiredCount;
    @Column(name = "currentCount")
    private int currentCount;

    public QuestProgressMobRequirement() {
    }

    public void setMobID(int mobID) {
        this.mobID = mobID;
    }

    public int getMobID() {
        return mobID;
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
        dos.writeInt(getMobID());
        dos.writeInt(getRequiredCount());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        QuestProgressMobRequirement qpmr = new QuestProgressMobRequirement();
        qpmr.setMobID(dis.readInt());
        qpmr.setRequiredCount(dis.readInt());
        return qpmr;
    }

    @Override
    public String getValue() {
        return String.valueOf(getCurrentCount());
    }
}