package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class FieldTemplate implements DatSerializable {
    private Rect rect = new Rect();
    protected String name = "", streetName = "";
    protected int id, returnMap, forcedReturn, fieldLimit;
    protected int mobRate, partyBonusR;
    protected int fixedMobCapacity, createMobInterval, timeOut, timeLimit, lvLimit, lvForceMove, consumeItemCoolTime, link;
    protected boolean town, swim, fly, isNeedSkillForFly, partyOnly, expeditionOnly, reactorShuffle;
    protected String onFirstUserEnter = "", onUserEnter = "";
    protected Set<Foothold> footholds = new HashSet<>();
    protected Set<Portal> portals = new HashSet<>();
    protected Set<LifeData> life = new HashSet<>();

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public void addLifeData(LifeData lifeData) {
        life.add(lifeData);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        System.out.println(id);
        dos.writeInt(rect.getLeft());
        dos.writeInt(rect.getTop());
        dos.writeInt(rect.getRight());
        dos.writeInt(rect.getBottom());
        dos.writeUTF(name);
        dos.writeUTF(streetName);
        dos.writeUTF(onFirstUserEnter);
        dos.writeUTF(onUserEnter);
        dos.writeInt(id);
        dos.writeInt(returnMap);
        dos.writeInt(forcedReturn);
        dos.writeInt(fieldLimit);
        dos.writeInt(mobRate);
        dos.writeInt(partyBonusR);
        dos.writeInt(fixedMobCapacity);
        dos.writeInt(createMobInterval);
        dos.writeInt(timeOut);
        dos.writeInt(timeLimit);
        dos.writeInt(lvLimit);
        dos.writeInt(lvForceMove);
        dos.writeInt(consumeItemCoolTime);
        dos.writeInt(link);
        dos.writeBoolean(town);
        dos.writeBoolean(swim);
        dos.writeBoolean(fly);
        dos.writeBoolean(isNeedSkillForFly);
        dos.writeBoolean(partyOnly);
        dos.writeBoolean(expeditionOnly);
        dos.writeBoolean(reactorShuffle);
        dos.writeInt(footholds.size());
        for(Foothold foothold : footholds) {
            foothold.write(dos);
        }
        dos.writeInt(portals.size());
        for (Portal portal : portals) {
            portal.write(dos);
        }
        dos.writeInt(life.size());
        for (LifeData i : life) {
            i.write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setRect(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        setName(dis.readUTF());
        setStreetName(dis.readUTF());
        setOnFirstUserEnter(dis.readUTF());
        setOnUserEnter(dis.readUTF());
        setId(dis.readInt());
        setReturnMap(dis.readInt());
        setForcedReturn(dis.readInt());
        setFieldLimit(dis.readInt());
        setMobRate(dis.readInt());
        setPartyBonusR(dis.readInt());
        setFixedMobCapacity(dis.readInt());
        setCreateMobInterval(dis.readInt());
        setTimeOut(dis.readInt());
        setTimeLimit(dis.readInt());
        setLvLimit(dis.readInt());
        setLvForceMove(dis.readInt());
        setConsumeItemCoolTime(dis.readInt());
        setLink(dis.readInt());
        setTown(dis.readBoolean());
        setSwim(dis.readBoolean());
        setFly(dis.readBoolean());
        setNeedSkillForFly(dis.readBoolean());
        setPartyOnly(dis.readBoolean());
        setExpeditionOnly(dis.readBoolean());
        setReactorShuffle(dis.readBoolean());
        int fSize = dis.readInt();
        for(int i = 0 ; i < fSize;i++) {
            footholds.add((Foothold) new Foothold().load(dis));
        }
        int pSize = dis.readInt();
        for (int i = 0; i < pSize; i++) {
            Portal portal = new Portal();
            portal.load(dis);
            getPortals().add(portal);
        }
        int lSize = dis.readInt();
        for (int i = 0; i < lSize; i++) {
            LifeData life = new LifeData();
            life.load(dis);
            getLife().add(life);
        }
        return this;
    }
}
