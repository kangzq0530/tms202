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

import com.msemu.commons.data.enums.ItemSpecStat;
import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/5/3.
 */
@Getter
@Setter
public class ItemSpec implements DatSerializable {


    protected Map<ItemSpecStat, Integer> specStats = new HashMap<>();
    protected List<Tuple<Integer, Integer>> indieQrPointTermStats = new ArrayList<>();
    protected List<Tuple<Integer, Integer>> availableMapRanges = new ArrayList<>();
    private List<Integer> petsCanFeed = new ArrayList<>();
    private List<Integer> randomPickup = new ArrayList<>();
    private List<Integer> realConsumeItems = new ArrayList<>();
    private String script = "", charColor = "", hue = "";
    private int type, npc, slotPerLine, slotCount, cosmetic, incTameness, incFatigue, incRepleteness, BFSkill, mobHp, mobID;
    private int startTime, interval, familiarPassiveSkillTarget, bs, bsUp, useLevel, skillID;
    private int recipe, reqSkillLevel, recipeUseCount, recipeValidDay, reqSkillProficiency, reqSkill;
    private boolean onlyPickup, runOnPickup, consumeOnPickup, repeatEffect, effectedOnAlly, randomPickupConsume, ignoreMixHair_jp, ignoreContinent, consumeDummy;
    private int exp, lifeId, moveTo, returnMapQR, criticalProb, gatherDropR, gatherDropRCount, effectSkillID;

    private FamiliarInfo familiarInfo = new FamiliarInfo();

    public void putSpecStat(ItemSpecStat ss, int i) {
        getSpecStats().put(ss, i);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getSpecStats().size());
        for (Map.Entry<ItemSpecStat, Integer> entry : getSpecStats().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeInt(entry.getValue());
        }
        dos.writeInt(indieQrPointTermStats.size());
        for (Tuple<Integer, Integer> pair : indieQrPointTermStats) {
            dos.writeInt(pair.getLeft());
            dos.writeInt(pair.getRight());
        }
        dos.writeInt(availableMapRanges.size());
        for (Tuple<Integer, Integer> pair : availableMapRanges) {
            dos.writeInt(pair.getLeft());
            dos.writeInt(pair.getRight());
        }
        dos.writeInt(petsCanFeed.size());
        for (Integer i : petsCanFeed) {
            dos.writeInt(i);
        }
        dos.writeInt(randomPickup.size());
        for (Integer i : randomPickup) {
            dos.writeInt(i);
        }
        dos.writeInt(realConsumeItems.size());
        for (Integer i : realConsumeItems) {
            dos.writeInt(i);
        }
        dos.writeUTF(script);
        dos.writeUTF(charColor);
        dos.writeUTF(hue);
        dos.writeInt(moveTo);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            ItemSpecStat stat = ItemSpecStat.valueOf(name);
            putSpecStat(stat, dis.readInt());
        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            indieQrPointTermStats.add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            availableMapRanges.add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            petsCanFeed.add(dis.readInt());
        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            randomPickup.add(dis.readInt());
        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            realConsumeItems.add(dis.readInt());
        }
        script = dis.readUTF();
        charColor = dis.readUTF();
        hue = dis.readUTF();
        moveTo = dis.readInt();
        return this;
    }
}
