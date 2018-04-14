package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMarriageRequirement implements IQuestStartRequirement {
    @Override
    public boolean hasRequirements(Character chr) {
        return chr.isMarried();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return new QuestStartMarriageRequirement();
    }
}
