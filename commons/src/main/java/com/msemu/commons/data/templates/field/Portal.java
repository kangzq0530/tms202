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

import com.msemu.commons.data.enums.PortalType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Position;
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
public class Portal implements DatSerializable {
    protected PortalType type;
    protected String name = "";
    protected String targetPortalName = "";
    protected String script = "";
    protected int delay;
    protected int id;
    protected int targetMapId = -1;
    protected Position position = new Position();
    protected int horizontalImpact;
    protected int verticalImpact;
    protected boolean onlyOnce;
    protected boolean hideTooltip;


    public Portal() {

    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(type.name());
        dos.writeUTF(name);
        dos.writeUTF(script);
        dos.writeUTF(targetPortalName);
        dos.writeInt(id);
        dos.writeInt(targetMapId);
        dos.writeInt(delay);
        dos.writeInt(position.getX());
        dos.writeInt(position.getY());
        dos.writeInt(horizontalImpact);
        dos.writeInt(verticalImpact);
        dos.writeBoolean(onlyOnce);
        dos.writeBoolean(hideTooltip);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setType(PortalType.valueOf(dis.readUTF()));
        setName(dis.readUTF());
        setScript(dis.readUTF());
        setTargetPortalName(dis.readUTF());
        setId(dis.readInt());
        setTargetMapId(dis.readInt());
        setDelay(dis.readInt());
        getPosition().setX(dis.readInt());
        getPosition().setY(dis.readInt());
        setHorizontalImpact(dis.readInt());
        setVerticalImpact(dis.readInt());
        setOnlyOnce(dis.readBoolean());
        setHideTooltip(dis.readBoolean());
        return this;
    }

}
