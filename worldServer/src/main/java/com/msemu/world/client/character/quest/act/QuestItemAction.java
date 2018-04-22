package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.items.Item;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.data.ItemData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestItemAction implements IQuestAction {
    private int id;
    private short quantity;
    private String potentialGrade;
    private int status;
    private int prop;
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }

    @Override
    public void action(Character chr) {
        Item item;
        if(ItemConstants.isEquip(getId())) {
            item = ItemData.getEquipFromTemplate(getId());
        } else {
            item = ItemData.getItemFromTemplate(getId());
            item.setQuantity(getQuantity());
        }
        chr.addItemToInventory(item);
    }

    public void setPotentialGrade(String potentialGrade) {
        this.potentialGrade = potentialGrade;
    }

    public String getPotentialGrade() {
        return potentialGrade;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }

    public int getProp() {
        return prop;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getId());
        dos.writeShort(getQuantity());
        dos.writeUTF(getPotentialGrade());
        dos.writeInt(getStatus());
        dos.writeInt(getProp());
        dos.writeInt(getGender());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        QuestItemAction qir = new QuestItemAction();
        qir.setId(dis.readInt());
        qir.setQuantity(dis.readShort());
        qir.setPotentialGrade(dis.readUTF());
        qir.setStatus(dis.readInt());
        qir.setProp(dis.readInt());
        qir.setGender(dis.readInt());
        return qir;
    }
}

