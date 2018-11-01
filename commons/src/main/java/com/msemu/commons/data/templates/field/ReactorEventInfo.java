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

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReactorEventInfo implements DatSerializable {

    private String act = "";

    private int type, nextState, timeOut, actCount;

    private List<Integer> args = new ArrayList<>();

    private List<String>  strings = new ArrayList<>();

    private List<Integer> activeSkills = new ArrayList<>();

    private ReactorClickArea clickArea = null;

    private Point lt, rb;


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(act);
        dos.writeInt(type);
        dos.writeInt(nextState);
        dos.writeInt(timeOut);
        dos.writeInt(actCount);
        dos.writeInt(args.size());
        for (Integer arg : args)
            dos.writeInt(arg);
        dos.writeInt(strings.size());
        for (String str : strings)
            dos.writeUTF(str);
        dos.writeInt(activeSkills.size());
        for (Integer skill : activeSkills)
            dos.writeInt(skill);
        dos.writeBoolean(clickArea != null);
        if (clickArea != null)
            clickArea.write(dos);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setAct(dis.readUTF());
        setType(dis.readInt());
        setNextState(dis.readInt());
        setTimeOut(dis.readInt());
        setActCount(dis.readInt());
        int argSize = dis.readInt();
        for (int i = 0; i < argSize; i++)
            getArgs().add(dis.readInt());
        int strSize = dis.readInt();
        for (int i = 0; i < strSize; i++)
            getStrings().add(dis.readUTF());
        int skillSize = dis.readInt();
        for (int i = 0; i < skillSize; i++)
            getActiveSkills().add(dis.readInt());
        if(dis.readBoolean()) {
            clickArea = new ReactorClickArea();
            clickArea.load(dis);
        }
        return this;
    }
}
