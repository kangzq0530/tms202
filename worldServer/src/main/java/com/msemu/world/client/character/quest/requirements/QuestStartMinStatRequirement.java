package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMinStatRequirement implements IQuestStartRequirement {

    private Stat stat;
    private short reqAmount;

    public QuestStartMinStatRequirement(Stat stat, short reqAmount) {
        this.reqAmount = reqAmount;
        this.stat = stat;
    }

    public QuestStartMinStatRequirement() {

    }

    private void setReqAmount(short reqAmount) {
        this.reqAmount = reqAmount;
    }

    private short getReqAmount() {
        return reqAmount;
    }

    private Stat getStat() {
        return stat;
    }

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getStat(getStat()) >= getReqAmount();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(stat.getValue());
        dos.writeShort(getReqAmount());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestStartMinStatRequirement(Stat.getByVal(dis.readInt()), dis.readShort());
    }
}
