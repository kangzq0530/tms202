package com.msemu.commons.data.templates;

import com.msemu.commons.data.enums.ItemGrade;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/28.
 */
@Getter
@Setter
public class ItemOptionInfo implements DatSerializable {

    private int id;
    private int optionType;
    private String string = "";
    private int reqLevel;

    @Getter
    private List<ItemOption> options = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(id);
        dos.writeInt(optionType);
        dos.writeInt(reqLevel);
        dos.writeUTF(string);
        dos.writeInt(options.size());
        for (ItemOption option : options) {
            option.write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setId(dis.readInt());
        setOptionType(dis.readInt());
        setReqLevel(dis.readInt());
        setString(dis.readUTF());
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            getOptions().add((ItemOption) new ItemOption().load(dis));
        }
        return this;
    }

    public boolean hasMatchingGrade(short itemState) {
        return ItemGrade.isMatching(itemState, ItemGrade.getGradeByOption(getId()).getValue());
    }

    public boolean isBonus() {
        return getId() / 1000 % 10 == 2;
    }
}
