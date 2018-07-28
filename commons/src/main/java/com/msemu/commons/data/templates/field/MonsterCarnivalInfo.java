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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public class MonsterCarnivalInfo implements DatSerializable {

    private int timeDefault, timeExpand, timeMessage, timeFinish;
    private String effectWin = "", effectLose = "", soundWin = "", soundLose = "";
    private int rewardMapWin, rewardMapLose;
    private boolean mapDivided;
    private int mobGenMax, deathCP, guardianGenMax;
    private int reactorRed, reactorBlue;
    private Set<CarnivalMobGenPos> mobGenPoses = new HashSet<>();
    private Set<CarnivalMob> mobs = new HashSet<>();
    private Set<CarnivalGuardianGenPos> guardianGenPoses = new HashSet<>();
    private Set<Integer> guardians = new HashSet<>();
    private Set<CarnivalReward> rewards = new HashSet<>();

    public void addGuardian(Integer index) {
        guardians.add(index);
    }

    public void addMobGenPos(CarnivalMobGenPos carnivalMobGenPos) {
        mobGenPoses.add(carnivalMobGenPos);
    }

    public void addMob(CarnivalMob mob) {
        mobs.add(mob);
    }

    public void addReward(CarnivalReward reward) {
        rewards.add(reward);
    }

    public void addGuardianGenPos(CarnivalGuardianGenPos carnivalGuardianGenPos) {
        guardianGenPoses.add(carnivalGuardianGenPos);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(timeDefault);
        dos.writeInt(timeExpand);
        dos.writeInt(timeMessage);
        dos.writeInt(timeFinish);
        dos.writeUTF(effectWin);
        dos.writeUTF(effectLose);
        dos.writeUTF(soundWin);
        dos.writeUTF(soundLose);
        dos.writeInt(rewardMapWin);
        dos.writeInt(rewardMapLose);
        dos.writeInt(mobGenMax);
        dos.writeBoolean(mapDivided);
        dos.writeInt(deathCP);
        dos.writeInt(guardianGenMax);
        dos.writeInt(reactorRed);
        dos.writeInt(reactorBlue);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setTimeDefault(dis.readInt());
        setTimeExpand(dis.readInt());
        setTimeMessage(dis.readInt());
        setTimeFinish(dis.readInt());
        setEffectWin(dis.readUTF());
        setEffectLose(dis.readUTF());
        setSoundWin(dis.readUTF());
        setSoundLose(dis.readUTF());
        setRewardMapWin(dis.readInt());
        setRewardMapLose(dis.readInt());
        setMobGenMax(dis.readInt());
        setMapDivided(dis.readBoolean());
        setDeathCP(dis.readInt());
        setGuardianGenMax(dis.readInt());
        setReactorRed(dis.readInt());
        setReactorBlue(dis.readInt());
        return this;
    }
}
