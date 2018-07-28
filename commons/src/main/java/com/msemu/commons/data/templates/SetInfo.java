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
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/6.
 */
@Getter
@Setter
public class SetInfo implements DatSerializable {
    public int incPDD, incMDD, incSTR, incDEX, incINT, incLUK, incACC, incPAD, incMAD,
            incSpeed, incEVA, incJump, incMHP, incMMP, incMHPr, incMMPr, incAllStat;
    private List<Tuple<Integer, Integer>> activeSkills = new ArrayList<>();
    private List<Tuple<Integer, Integer>> options = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(incPDD);
        dos.writeInt(incMDD);
        dos.writeInt(incSTR);
        dos.writeInt(incDEX);
        dos.writeInt(incINT);
        dos.writeInt(incLUK);
        dos.writeInt(incACC);
        dos.writeInt(incPAD);
        dos.writeInt(incMAD);
        dos.writeInt(incSpeed);
        dos.writeInt(incEVA);
        dos.writeInt(incJump);
        dos.writeInt(incMHP);
        dos.writeInt(incMMP);
        dos.writeInt(incMHPr);
        dos.writeInt(incMMPr);
        dos.writeInt(incAllStat);
        dos.writeInt(activeSkills.size());
        for (Tuple<Integer, Integer> tup : activeSkills) {
            dos.writeInt(tup.getLeft());
            dos.writeInt(tup.getRight());
        }
        dos.writeInt(options.size());
        for (Tuple<Integer, Integer> tup : options) {
            dos.writeInt(tup.getLeft());
            dos.writeInt(tup.getRight());
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setIncPDD(dis.readInt());
        setIncMDD(dis.readInt());
        setIncSTR(dis.readInt());
        setIncDEX(dis.readInt());
        setIncINT(dis.readInt());
        setIncLUK(dis.readInt());
        setIncACC(dis.readInt());
        setIncPAD(dis.readInt());
        setIncMAD(dis.readInt());
        setIncSpeed(dis.readInt());
        setIncEVA(dis.readInt());
        setIncJump(dis.readInt());
        setIncMHP(dis.readInt());
        setIncMMP(dis.readInt());
        setIncMHPr(dis.readInt());
        setIncMMPr(dis.readInt());
        setIncAllStat(dis.readInt());
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            getActiveSkills().add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        int size2 = dis.readInt();
        for (int i = 0; i < size2; i++) {
            getOptions().add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        return this;
    }
}
