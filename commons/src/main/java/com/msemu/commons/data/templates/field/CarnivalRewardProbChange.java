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
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public class CarnivalRewardProbChange implements DatSerializable {
    private float winCoin, winRecovery, winNuff, winCP;
    private float loseCoin, loseRecovery, loseNuff, loseCP;

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeFloat(winCoin);
        dos.writeFloat(winRecovery);
        dos.writeFloat(winNuff);
        dos.writeFloat(winCP);
        dos.writeFloat(loseCoin);
        dos.writeFloat(loseRecovery);
        dos.writeFloat(loseNuff);
        dos.writeFloat(loseCP);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setWinCoin(dis.readFloat());
        setWinRecovery(dis.readFloat());
        setWinNuff(dis.readFloat());
        setWinCP(dis.readFloat());
        setLoseCoin(dis.readFloat());
        setLoseRecovery(dis.readFloat());
        setLoseNuff(dis.readFloat());
        setLoseCP(dis.readFloat());
        return this;
    }
}
