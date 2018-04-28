package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
@Getter
@Setter
public class QuestItemReqData  extends QuestReqData {

    private int itemId;
    private int quantity;
    private int order;

    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.item;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(itemId);
        dos.writeInt(quantity);
        dos.writeInt(order);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setItemId(dis.readInt());
        setQuantity(dis.readInt());
        setOrder(dis.readInt());
        return this;
    }
}
