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
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class FieldTemplate implements DatSerializable {
    private Rect rect = new Rect();
    private String name = "", streetName = "";
    private int id, returnMap, forcedReturn, fieldLimit;
    private int mobRate, partyBonusR;
    private int fixedMobCapacity, createMobInterval, timeOut, timeLimit, lvLimit, lvForceMove, consumeItemCoolTime, link, bossMobID;
    private boolean town, swim, fly, isNeedSkillForFly, partyOnly, expeditionOnly, reactorShuffle;
    private String onFirstUserEnter = "", onUserEnter = "";
    private Set<Foothold> footholds = new HashSet<>();
    private Set<Portal> portals = new HashSet<>();
    private Set<LifeInField> life = new HashSet<>();
    private HashMap<Integer, LifeInField> categoryLife = new HashMap<>();
    private Set<RadderRope> radderRopes = new HashSet<>();
    private List<FieldArea> areas = new ArrayList<>();
    private Set<FieldDirectionInfo> directionInfos = new HashSet<>();
    private MonsterCarnivalInfo monsterCarnivalInfo = null;
    private FieldNodeInfo fieldNodeInfo = new FieldNodeInfo();
    private Set<FieldObjectInfo> objects = new HashSet<>();
    private List<ReactorInField> reactorsInfo = new ArrayList<>();

    public boolean hasCarnivalInfo() {
        return monsterCarnivalInfo != null;
    }

    public void addObject(FieldObjectInfo object) {
        objects.add(object);
    }

    public void addDirectionInfo(FieldDirectionInfo directionInfo) {
        directionInfos.add(directionInfo);
    }

    public void addRadderRope(RadderRope radderRope) {
        radderRopes.add(radderRope);
    }

    public void addArea(FieldArea area) {
        areas.add(area);
    }

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public void addLifeData(LifeInField lifeInField) {
        life.add(lifeInField);
    }

    public void addLifeData(Integer category, LifeInField lifeInField) {
        categoryLife.put(category, lifeInField);
    }

    public List<FieldObjectInfo> getMovingPlatforms() {
        return getObjects().stream()
                .filter(FieldObjectInfo::isMove)
                .collect(Collectors.toList());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
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
        dos.writeInt(bossMobID);
        dos.writeBoolean(town);
        dos.writeBoolean(swim);
        dos.writeBoolean(fly);
        dos.writeBoolean(isNeedSkillForFly);
        dos.writeBoolean(partyOnly);
        dos.writeBoolean(expeditionOnly);
        dos.writeBoolean(reactorShuffle);
        dos.writeInt(footholds.size());
        for (Foothold foothold : footholds) {
            foothold.write(dos);
        }
        dos.writeInt(portals.size());
        for (Portal portal : portals) {
            portal.write(dos);
        }
        dos.writeInt(life.size());
        for (LifeInField i : life) {
            i.write(dos);
        }
        dos.writeInt(radderRopes.size());
        for (RadderRope radderRope : radderRopes) {
            radderRope.write(dos);
        }
        dos.writeInt(areas.size());
        for (FieldArea area : areas) {
            area.write(dos);
        }
        dos.writeInt(directionInfos.size());
        for (FieldDirectionInfo directionInfo : directionInfos) {
            directionInfo.write(dos);
        }
        dos.writeBoolean(hasCarnivalInfo());
        if (hasCarnivalInfo()) {
            monsterCarnivalInfo.write(dos);
        }
        fieldNodeInfo.write(dos);
        dos.writeInt(objects.size());
        for (FieldObjectInfo object : objects) {
            object.write(dos);
        }
        dos.writeInt(reactorsInfo.size());
        for(ReactorInField ri : reactorsInfo) {
            ri.write(dos);
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
        setBossMobID(dis.readInt());
        setTown(dis.readBoolean());
        setSwim(dis.readBoolean());
        setFly(dis.readBoolean());
        setNeedSkillForFly(dis.readBoolean());
        setPartyOnly(dis.readBoolean());
        setExpeditionOnly(dis.readBoolean());
        setReactorShuffle(dis.readBoolean());
        int fSize = dis.readInt();
        for (int i = 0; i < fSize; i++) {
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
            LifeInField life = new LifeInField();
            life.load(dis);
            getLife().add(life);
        }
        int rSize = dis.readInt();
        for (int i = 0; i < rSize; i++) {
            RadderRope radderRope = new RadderRope();
            radderRope.load(dis);
            addRadderRope(radderRope);
        }
        int aSize = dis.readInt();
        for (int i = 0; i < aSize; i++) {
            FieldArea area = new FieldArea();
            area.load(dis);
            addArea(area);
        }
        int dSize = dis.readInt();
        for (int i = 0; i < dSize; i++) {
            FieldDirectionInfo directionInfo = new FieldDirectionInfo();
            directionInfo.load(dis);
            addDirectionInfo(directionInfo);
        }
        boolean hasCarnival = dis.readBoolean();
        if (hasCarnival) {
            MonsterCarnivalInfo carnivalInfo = new MonsterCarnivalInfo();
            carnivalInfo.load(dis);
            setMonsterCarnivalInfo(carnivalInfo);
        }
        fieldNodeInfo.load(dis);

        int obSize = dis.readInt();
        for (int i = 0; i < obSize; i++) {
            FieldObjectInfo object = new FieldObjectInfo();
            object.load(dis);
            addObject(object);
        }
        int reSize = dis.readInt();
        for(int i = 0 ; i < reSize; i++) {
            ReactorInField ri = new ReactorInField();
            ri.load(dis);
            getReactorsInfo().add(ri);
        }
        return this;
    }
}
