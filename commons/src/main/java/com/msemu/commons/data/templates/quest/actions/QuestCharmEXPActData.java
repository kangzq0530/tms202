package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestCharmEXPActData extends QuestActData {

    @Getter
    @Setter
    private int charmEXP;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.charmEXP;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(charmEXP);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setCharmEXP(dis.readInt());
        return this;
    }
}
