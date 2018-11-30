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

@Getter
@Setter
public class ReactorClickArea implements DatSerializable {
    private Point lt;
    private Point rb;
    private String message = "";

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(message);
        dos.writeBoolean(lt != null);
        if (lt != null) {
            dos.writeInt((int) lt.getX());
            dos.writeInt((int) lt.getY());
        }
        dos.writeBoolean(rb != null);
        if (rb != null) {
            dos.writeInt((int) rb.getX());
            dos.writeInt((int) rb.getY());
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {

        if (dis.readBoolean()) {
            lt = new Point(dis.readInt(), dis.readInt());
        }
        if (dis.readBoolean()) {
            rb = new Point(dis.readInt(), dis.readInt());
        }
        return this;
    }
}