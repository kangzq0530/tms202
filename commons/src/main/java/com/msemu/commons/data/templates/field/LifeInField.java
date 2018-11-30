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

package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
@Setter
public class LifeInField implements DatSerializable {
    private String type = "";
    private int id;
    private int x, y, f, fh, cy, rx0, rx1;
    private int mobTime, mobAliveReq, regenStart;
    private boolean hide, useDay, useNight, hold, nofoothold, dummy, spine, mobTimeOnDie;
    private String limitedname = "";

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(type);
        dos.writeUTF(limitedname);
        dos.writeInt(id);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(f);
        dos.writeInt(fh);
        dos.writeInt(cy);
        dos.writeInt(rx0);
        dos.writeInt(rx1);
        dos.writeInt(mobTime);
        dos.writeInt(mobAliveReq);
        dos.writeInt(regenStart);
        dos.writeBoolean(hide);
        dos.writeBoolean(useDay);
        dos.writeBoolean(useNight);
        dos.writeBoolean(hold);
        dos.writeBoolean(nofoothold);
        dos.writeBoolean(dummy);
        dos.writeBoolean(spine);
        dos.writeBoolean(mobTimeOnDie);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setType(dis.readUTF());
        setLimitedname(dis.readUTF());
        setId(dis.readInt());
        setX(dis.readInt());
        setY(dis.readInt());
        setF(dis.readInt());
        setFh(dis.readInt());
        setCy(dis.readInt());
        setRx0(dis.readInt());
        setRx1(dis.readInt());
        setMobTime(dis.readInt());
        setMobAliveReq(dis.readInt());
        setRegenStart(dis.readInt());
        setHide(dis.readBoolean());
        setUseDay(dis.readBoolean());
        setUseNight(dis.readBoolean());
        setHold(dis.readBoolean());
        setNofoothold(dis.readBoolean());
        setDummy(dis.readBoolean());
        setSpine(dis.readBoolean());
        setMobTimeOnDie(dis.readBoolean());
        return this;
    }
}
