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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/5/6.
 */
@Getter
@Setter
public class MonsterBook implements DatSerializable {
    private int mobTemplateID;
    private String episode = "";
    private Set<Integer> maps = new HashSet<>();
    private Set<Integer> rewards = new HashSet<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(mobTemplateID);
        dos.writeUTF(episode);
        dos.writeInt(maps.size());
        for (Integer v : maps)
            dos.writeInt(v);
        dos.writeInt(rewards.size());
        for (Integer v : rewards)
            dos.writeInt(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setMobTemplateID(dis.readInt());
        setEpisode(dis.readUTF());
        int size = dis.readInt();
        for (int i = 0; i < size; i++)
            maps.add(dis.readInt());
        int size2 = dis.readInt();
        for (int i = 0; i < size2; i++)
            rewards.add(dis.readInt());
        return this;
    }
}
