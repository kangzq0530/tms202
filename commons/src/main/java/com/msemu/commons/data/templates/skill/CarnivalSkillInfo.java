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

package com.msemu.commons.data.templates.skill;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class CarnivalSkillInfo implements DatSerializable{
    private int target;
    private int skillID;
    private int level;
    private String name;
    private String desc;


    @Override
    public String toString() {
        return String.format("[怪物擂台技能] %s - $s (%d) 等級: %d", name, desc, skillID, level);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.target);
        dos.writeInt(this.skillID);
        dos.writeInt(this.level);
        dos.writeUTF(this.name);
        dos.writeUTF(this.desc);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this .setTarget(dis.readInt());
        this.setSkillID(dis.readInt());
        this.setLevel(dis.readInt());
        this.setName(dis.readUTF());
        this.setDesc(dis.readUTF());
        return this;
    }
}
