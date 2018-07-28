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
 * Created by Weber on 2018/5/7.
 */
@Getter
public class VCoreEnforcementEntry implements DatSerializable {

    private int level;
    private int nextExp;
    private int expEnforce;
    private int extract;

    public VCoreEnforcementEntry() {
    }

    public VCoreEnforcementEntry(int level, int nextExp, int expEnforce, int extract) {
        this.level = level;
        this.nextExp = nextExp;
        this.expEnforce = expEnforce;
        this.extract = extract;
    }


    @Override
    public String toString() {
        return "[V技能強化資料] 等級：" + this.level + " 升級所需經驗：" + this.nextExp + " 強化經驗：" + this.expEnforce + " 分解碎片：" + this.extract;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(level);
        dos.writeInt(nextExp);
        dos.writeInt((expEnforce));
        dos.writeInt(extract);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.level = dis.readInt();
        this.nextExp = dis.readInt();
        this.expEnforce = dis.readInt();
        this.extract = dis.readInt();
        return this;
    }
}
