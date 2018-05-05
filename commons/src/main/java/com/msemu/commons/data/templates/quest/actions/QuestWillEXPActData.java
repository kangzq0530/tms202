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
public class QuestWillEXPActData extends QuestActData {

    @Getter
    @Setter
    private int willEXP;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.willEXP;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(willEXP);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setWillEXP(dis.readInt());
        return this;
    }
}