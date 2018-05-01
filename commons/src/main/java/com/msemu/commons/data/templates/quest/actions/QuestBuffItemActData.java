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
public class QuestBuffItemActData extends QuestActData {

    @Getter
    @Setter
    private int buffItemID;

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.buffItemID;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(buffItemID);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setBuffItemID(dis.readInt());
        return this;
    }
}
