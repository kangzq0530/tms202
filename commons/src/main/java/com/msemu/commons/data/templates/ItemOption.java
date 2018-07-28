/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.commons.data.templates;


import com.msemu.commons.data.enums.ItemOptionStat;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class ItemOption implements DatSerializable {

    private int optionType;
    @Getter(AccessLevel.NONE)
    private String string = "";
    private int reqLevel;

    private int level;
    private String face = "";

    private Map<ItemOptionStat, Integer> otherData = new HashMap<>();

    public static ItemOptionStat[] getTypes() {
        return ItemOptionStat.values();
    }

    public void setLevelData(ItemOptionStat type, Integer value) {
        otherData.put(type, value);
    }

    @Override
    public String toString() {
        return "level: " + getLevel() + ", optionType: " + getOptionType() + " : " + getString();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.optionType);
        dos.writeUTF(this.string);
        dos.writeInt(this.level);
        dos.writeInt(this.reqLevel);
        dos.writeUTF(this.face);
        dos.writeInt(otherData.size());
        for (Map.Entry<ItemOptionStat, Integer> entry : this.otherData.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeInt(entry.getValue());
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setOptionType(dis.readInt());
        this.setString(dis.readUTF());
        this.setLevel(dis.readInt());
        this.setReqLevel(dis.readInt());
        this.setFace(dis.readUTF());
        int otherDataSize = dis.readInt();
        for (int i = 0; i < otherDataSize; i++) {
            String name = dis.readUTF();
            Integer value = dis.readInt();
            ItemOptionStat stat = ItemOptionStat.valueOf(name);
            this.getOtherData().put(stat, value);
        }
        return this;
    }

    public String getString() {
        final String[] ret = {this.string};
        getOtherData().forEach((key, value) -> {
            ret[0] = ret[0].replace("#" + key, value.toString());
        });
        return ret[0];
    }

}
