package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMaxLevelRequirement implements IQuestStartRequirement {

    private short level;

    public QuestStartMaxLevelRequirement(short level) {
        this.level = level;
    }

    public QuestStartMaxLevelRequirement() {

    }

    private short getLevel() {
        return level;
    }

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getLevel() <= getLevel();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeShort(getLevel());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestStartMaxLevelRequirement(dis.readShort());
    }
}
