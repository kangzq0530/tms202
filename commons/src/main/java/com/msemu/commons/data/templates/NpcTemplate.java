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

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/22.
 */

public class NpcTemplate implements DatSerializable {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String name = "";
    @Getter
    @Setter
    private String script = "";

    @Override
    public String toString() {
        return String.format("[NPC] %s(%d) Scripts: %s", getName(), getId(), getScript());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        dos.writeUTF(this.script);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setId(dis.readInt());
        this.setName(dis.readUTF());
        this.setScript(dis.readUTF());
        return this;
    }
}
