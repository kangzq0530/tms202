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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/7.
 */
public class VCoreInfo implements DatSerializable {

    private final Map<Integer, VCoreEnforcementEntry> skill = new HashMap<>();
    private final Map<Integer, VCoreEnforcementEntry> enhance = new HashMap<>();
    private final Map<Integer, VCoreEnforcementEntry> special = new HashMap<>();

    public void addSkill(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }

    public void addEnhance(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }

    public void addSpecial(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(skill.size());
        for (Map.Entry<Integer, VCoreEnforcementEntry> entry : skill.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.writeInt(enhance.size());
        for (Map.Entry<Integer, VCoreEnforcementEntry> entry : enhance.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.writeInt(special.size());
        for (Map.Entry<Integer, VCoreEnforcementEntry> entry : special.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int size1 = dis.readInt();
        for (int i = 0; i < size1; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        int size2 = dis.readInt();
        for (int i = 0; i < size2; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        int size3 = dis.readInt();
        for (int i = 0; i < size3; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        return this;
    }
}
