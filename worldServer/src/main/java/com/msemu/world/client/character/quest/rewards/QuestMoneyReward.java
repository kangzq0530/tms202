package com.msemu.world.client.character.quest.rewards;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestMoneyReward implements IQuestReward {
    private long money;

    public QuestMoneyReward(long money) {
        this.money = money;
    }

    public QuestMoneyReward() {

    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }


    @Override
    public void giveReward(Character chr) {
        chr.addMoney(money);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(getMoney());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestMoneyReward(dis.readLong());
    }
}

