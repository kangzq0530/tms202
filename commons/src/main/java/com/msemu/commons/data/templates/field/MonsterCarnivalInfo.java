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
