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
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReactorTemplate implements DatSerializable {

    private int id;

    private String viewName = "", action = "";

    private int resetTime, level, link;

    private boolean frontTile, backTile, activateByTouch, notFatigue, forcedViewName;

    private List<ReactorStateInfo> statesInfo = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(viewName);
        dos.writeUTF(action);
        dos.writeInt(id);
        dos.writeInt(resetTime);
        dos.writeInt(level);
        dos.writeInt(link);
        dos.writeBoolean(frontTile);
        dos.writeBoolean(backTile);
        dos.writeBoolean(activateByTouch);
        dos.writeBoolean(notFatigue);
        dos.writeBoolean(forcedViewName);

        dos.writeInt(statesInfo.size());

        for(ReactorStateInfo stateInfo : statesInfo)
        {
            stateInfo.write(dos);
        }

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setViewName(dis.readUTF());
        setAction(dis.readUTF());
        setId(dis.readInt());
        setResetTime(dis.readInt());
        setLevel(dis.readInt());
        setLink(dis.readInt());
        setFrontTile(dis.readBoolean());
        setBackTile(dis.readBoolean());
        setActivateByTouch(dis.readBoolean());
        setNotFatigue(dis.readBoolean());
        setForcedViewName(dis.readBoolean());
        int statesSize = dis.readInt();
        for(int i = 0 ; i < statesSize;  i++){
            ReactorStateInfo state = new ReactorStateInfo();
            state.load(dis);
            getStatesInfo().add(state);
        }
        return this;
    }
}
