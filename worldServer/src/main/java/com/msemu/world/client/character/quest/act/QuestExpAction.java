package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestExpAction implements IQuestAction {
    private long exp;

    public QuestExpAction(long exp) {
        this.exp = exp;
    }

    public QuestExpAction() {

    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getExp() {
        return exp;
    }

    @Override
    public void action(Character chr) {
        chr.addExp(getExp());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(getExp());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestExpAction(dis.readLong());
    }
}
