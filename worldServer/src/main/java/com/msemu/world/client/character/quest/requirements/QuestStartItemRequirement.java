package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartItemRequirement implements IQuestStartRequirement {
    private int id;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addReqItem(int reqItem, int count) {
        setId(reqItem);
        setQuantity(count);
    }

    @Override
    public boolean hasRequirements(Character chr) {
        return chr.hasItemCount(getId(), getQuantity());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getId());
        dos.writeInt(getQuantity());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        QuestStartItemRequirement qsir = new QuestStartItemRequirement();
        qsir.setId(dis.readInt());
        qsir.setQuantity(dis.readInt());
        return qsir;
    }
}
