package com.msemu.world.client.character.quest.rewards;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestPopReward implements IQuestReward {

    private int pop;

    public QuestPopReward(int pop) {
        this.pop = pop;
    }

    public QuestPopReward() {

    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    @Override
    public void giveReward(Character chr) {
        chr.addStat(Stat.POP, getPop());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getPop());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestPopReward(dis.readInt());
    }
}
