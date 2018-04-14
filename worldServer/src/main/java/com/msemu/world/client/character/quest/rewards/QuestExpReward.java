package com.msemu.world.client.character.quest.rewards;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestExpReward implements IQuestReward {
    private long exp;

    public QuestExpReward(long exp) {
        this.exp = exp;
    }

    public QuestExpReward() {

    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getExp() {
        return exp;
    }

    @Override
    public void giveReward(Character chr) {
        chr.addExp(getExp());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(getExp());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestExpReward(dis.readLong());
    }
}
