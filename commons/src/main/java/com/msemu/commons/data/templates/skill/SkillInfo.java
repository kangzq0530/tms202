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

import com.msemu.commons.data.enums.Element;
import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/23.
 */

public class SkillInfo implements DatSerializable {
    @Getter
    @Setter
    private String name = "", desc = "";
    @Getter
    @Setter
    private int rootId, skillId, masterLevel, fixLevel, skillType = -1, eventTamingMob = 0, vehicleID = 0, hyper = 0, hyperStat = 0, reqLev = 0, setItemReason = 0, setItemPartsCount = 0;
    @Getter
    @Setter
    private boolean finalAttack, massSpell, invisible = false, notRemoved = false, timeLimited = false, combatOrders = false, psd = false, vSkill = false, petPassive = false;
    // info prop
    @Getter
    @Setter
    private boolean pvp = true, magicDamage, casterMove, pushTarget, pullTarget, keyDownSkill, summonSkill;
    @Getter
    @Setter
    private Element elemAttr = Element.NEUTRAL;
    @Getter
    private List<ReqSkill> reqSkill = new ArrayList<>();
    @Getter
    private List<Integer> psdSkills = new ArrayList<>();
    @Getter
    private Map<Integer, Map<SkillStat, Integer>> psdWT = new HashMap<>();
    @Getter
    private List<Integer> finalAttackSkills = new ArrayList<>();

    // common and level
    @Setter
    private Rect rect1 = null, rect2 = null, rect3 = null, rect4 = null;
    private Map<SkillStat, String> stats = new HashMap<>();
    private Map<Integer, Map<SkillStat, String>> statsByLevel = new HashMap<>();
    private Map<Integer, Rect> rect1ByLevel = new HashMap<>();
    private Map<Integer, Rect> rect2ByLevel = new HashMap<>();
    private Map<Integer, Rect> rect3ByLevel = new HashMap<>();
    private Map<Integer, Rect> rect4ByLevel = new HashMap<>();


    public void addSkillStat(SkillStat stat, String value) {
        stats.put(stat, value);
    }

    public void addSkillStatByLevel(int slv, SkillStat stat, String value) {
        if (!statsByLevel.containsKey(slv))
            statsByLevel.put(slv, new HashMap<>());
        statsByLevel.get(slv).put(stat, value);
    }

    public int getValue(SkillStat stat, int slv) {
        int result;
        if (!statsByLevel.containsKey(slv) && !stats.containsKey(stat))
            return 0;
        String value = stats.getOrDefault(stat, null);
        if (value == null && statsByLevel.containsKey(slv)) {
            statsByLevel.get(slv).get(stat);
        }
        if (value == null) {
            return 0;
        }
        if (StringUtils.isNumber(value)) {
            result = Integer.parseInt(value);
        } else {
            result = evaluateValue(value, slv);
        }
        return result;
    }

    public Rect getRect() {
        return getRect(0);
    }

    public Rect getRect(int slv) {
        if (rect1 == null)
            return rect1ByLevel.getOrDefault(slv, null);
        return rect1;
    }

    public Rect getRect2() {
        return getRect2(0);
    }

    public Rect getRect2(int slv) {
        if (rect2 == null)
            return rect2ByLevel.getOrDefault(slv, null);
        return rect2;
    }

    public Rect getRect3() {
        return getRect3(0);
    }

    public Rect getRect3(int slv) {
        if (rect3 == null)
            return rect3ByLevel.getOrDefault(slv, null);
        return rect3;
    }

    public Rect getRect4() {
        return getRect4(0);
    }

    public Rect getRect4(int slv) {
        if (rect4 == null)
            return rect4ByLevel.getOrDefault(slv, null);
        return rect4;
    }

    private int evaluateValue(String expression, int slv) {
        int result = 0;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            String newExpression = expression.replace("u", "Math.ceil")
                    .replace("d", "Math.floor")
                    .replace("x", String.valueOf(slv));
            Object res = engine.eval(newExpression);
            if (res instanceof Integer) {
                result = (Integer) res;
            } else if (res instanceof Double) {
                result = ((Double) res).intValue();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addPsdSkill(int skillID) {
        getPsdSkills().add(skillID);
    }

    public void addReqSkill(ReqSkill req) {
        getReqSkill().add(req);
    }

    public void addRectByLevel(int slv, Rect rect) {
        rect1ByLevel.put(slv, rect);
    }

    public void addRect2ByLevel(int slv, Rect rect) {
        rect2ByLevel.put(slv, rect);
    }

    public void addRect3ByLevel(int slv, Rect rect) {
        rect3ByLevel.put(slv, rect);
    }

    public void addRect4ByLevel(int slv, Rect rect) {
        rect4ByLevel.put(slv, rect);
    }

    public int getMaxLevel() {
        if (stats.containsKey(SkillStat.maxLevel))
            return getValue(SkillStat.maxLevel, 0);
        else
            return statsByLevel.size();
    }

    @Override
    public String toString() {
        return String.format("[技能] %s(%d) - %s", name, skillId, desc);
    }

    @Override
    public void write(final DataOutputStream dos) throws IOException {
        dos.writeUTF(this.name);
        dos.writeUTF(this.desc);
        dos.writeUTF(this.elemAttr.name());
        dos.writeInt(this.rootId);
        dos.writeInt(this.skillId);
        dos.writeInt(this.masterLevel);
        dos.writeInt(this.fixLevel);
        dos.writeInt(this.skillType);
        dos.writeInt(this.eventTamingMob);
        dos.writeInt(this.vehicleID);
        dos.writeInt(this.hyper);
        dos.writeInt(this.hyperStat);
        dos.writeInt(this.reqLev);
        dos.writeInt(this.setItemReason);
        dos.writeInt(this.setItemPartsCount);
        dos.writeBoolean(this.massSpell);
        dos.writeBoolean(this.invisible);
        dos.writeBoolean(this.notRemoved);
        dos.writeBoolean(this.timeLimited);
        dos.writeBoolean(this.combatOrders);
        dos.writeBoolean(this.psd);
        dos.writeBoolean(this.vSkill);
        dos.writeBoolean(this.petPassive);
        dos.writeBoolean(this.pvp);
        dos.writeBoolean(this.magicDamage);
        dos.writeBoolean(this.casterMove);
        dos.writeBoolean(this.pushTarget);
        dos.writeBoolean(this.pullTarget);
        dos.writeBoolean(this.keyDownSkill);
        dos.writeBoolean(this.summonSkill);
        dos.writeBoolean(this.finalAttack);

        dos.writeInt(reqSkill.size());
        for (ReqSkill req : reqSkill) {
            req.write(dos);
        }
        dos.writeInt(psdSkills.size());
        for (Integer value : psdSkills) {
            dos.writeInt(value);
        }
        Rect[] rects = {rect1, rect2, rect3, rect4};
        for (Rect rect : rects) {
            dos.writeBoolean(rect != null);
            if (rect != null) {
                dos.writeInt(rect.getLeft());
                dos.writeInt(rect.getTop());
                dos.writeInt(rect.getRight());
                dos.writeInt(rect.getBottom());
            }
        }

        writeRectByLevels(dos, rect1ByLevel);
        writeRectByLevels(dos, rect2ByLevel);
        writeRectByLevels(dos, rect3ByLevel);
        writeRectByLevels(dos, rect4ByLevel);

        dos.writeInt(stats.size());
        for (Map.Entry<SkillStat, String> entry : stats.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
        dos.writeInt(statsByLevel.size());
        for (Map.Entry<Integer, Map<SkillStat, String>> entry : statsByLevel.entrySet()) {
            dos.writeInt(entry.getKey());
            dos.writeInt(entry.getValue().size());
            for (Map.Entry<SkillStat, String> subEntry : entry.getValue().entrySet()) {
                dos.writeUTF(subEntry.getKey().name());
                dos.writeUTF(subEntry.getValue());
            }
        }
        dos.writeInt(psdWT.size());
        for (Map.Entry<Integer, Map<SkillStat, Integer>> entry : psdWT.entrySet()) {
            dos.writeInt(entry.getKey());
            dos.writeInt(entry.getValue().size());
            for (Map.Entry<SkillStat, Integer> subEntry : entry.getValue().entrySet()) {
                dos.writeUTF(subEntry.getKey().name());
                dos.writeInt(subEntry.getValue());
            }
        }
        dos.writeInt(finalAttackSkills.size());
        for (Integer skillId : finalAttackSkills) {
            dos.writeInt(skillId);
        }
    }

    private void writeRectByLevels(DataOutputStream dos, Map<Integer, Rect> data) throws IOException {
        dos.writeInt(data.size());
        for (Map.Entry<Integer, Rect> entry : data.entrySet()) {
            dos.writeInt(entry.getKey());
            dos.writeInt(entry.getValue().getLeft());
            dos.writeInt(entry.getValue().getTop());
            dos.writeInt(entry.getValue().getRight());
            dos.writeInt(entry.getValue().getBottom());
        }
    }


    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setName(dis.readUTF());
        setDesc(dis.readUTF());
        setElemAttr(Element.valueOf(dis.readUTF()));
        setRootId(dis.readInt());
        setSkillId(dis.readInt());
        setMasterLevel(dis.readInt());
        setFixLevel(dis.readInt());
        setSkillType(dis.readInt());
        setEventTamingMob(dis.readInt());
        setVehicleID(dis.readInt());
        setHyper(dis.readInt());
        setHyperStat(dis.readInt());
        setReqLev(dis.readInt());
        setSetItemReason(dis.readInt());
        setSetItemPartsCount(dis.readInt());
        setMassSpell(dis.readBoolean());
        setInvisible(dis.readBoolean());
        setNotRemoved(dis.readBoolean());
        setTimeLimited(dis.readBoolean());
        setCombatOrders(dis.readBoolean());
        setPsd(dis.readBoolean());
        setVSkill(dis.readBoolean());
        setPetPassive(dis.readBoolean());
        setPvp(dis.readBoolean());
        setMagicDamage(dis.readBoolean());
        setCasterMove(dis.readBoolean());
        setPushTarget(dis.readBoolean());
        setPullTarget(dis.readBoolean());
        setKeyDownSkill(dis.readBoolean());
        setSummonSkill(dis.readBoolean());
        setFinalAttack(dis.readBoolean());

        int reqSkillSize = dis.readInt();
        for (int i = 0; i < reqSkillSize; i++) {
            getReqSkill().add((ReqSkill) new ReqSkill().load(dis));
        }
        int psdSkillSize = dis.readInt();
        for (int i = 0; i < psdSkillSize; i++) {
            getPsdSkills().add(dis.readInt());
        }
        if (dis.readBoolean()) {
            setRect1(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        }
        if (dis.readBoolean()) {
            setRect2(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        }
        if (dis.readBoolean()) {
            setRect3(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        }
        if (dis.readBoolean()) {
            setRect4(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        }

        int rect1ByLevelSize = dis.readInt();
        for (int i = 0; i < rect1ByLevelSize; i++) {
            int key = dis.readInt();
            Rect rect = new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt());
            rect1ByLevel.put(key, rect);
        }
        int rect2ByLevelSize = dis.readInt();
        for (int i = 0; i < rect2ByLevelSize; i++) {
            int key = dis.readInt();
            Rect rect = new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt());
            rect2ByLevel.put(key, rect);
        }
        int rect3ByLevelSize = dis.readInt();
        for (int i = 0; i < rect3ByLevelSize; i++) {
            int key = dis.readInt();
            Rect rect = new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt());
            rect3ByLevel.put(key, rect);
        }
        int rect4ByLevelSize = dis.readInt();
        for (int i = 0; i < rect4ByLevelSize; i++) {
            int key = dis.readInt();
            Rect rect = new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt());
            rect4ByLevel.put(key, rect);
        }

        int statsSize = dis.readInt();
        for (int i = 0; i < statsSize; i++) {
            String key = dis.readUTF();
            String value = dis.readUTF();
            stats.put(SkillStat.valueOf(key), value);
        }

        int statsByLevelSize = dis.readInt();
        for (int i = 0; i < statsByLevelSize; i++) {
            int slv = dis.readInt();
            statsByLevel.put(slv, new EnumMap<>(SkillStat.class));
            int size = dis.readInt();
            for (int j = 0; j < size; j++)
                statsByLevel.get(slv).put(SkillStat.valueOf(dis.readUTF()), dis.readUTF());
        }

        int psdWTSize = dis.readInt();
        for (int i = 0; i < psdWTSize; i++) {
            int wt = dis.readInt();
            getPsdWT().put(wt, new EnumMap<>(SkillStat.class));
            int size = dis.readInt();
            for (int j = 0; j < size; j++) {
                getPsdWT().get(wt).put(SkillStat.valueOf(dis.readUTF()), dis.readInt());
            }
        }
        int finalSkillIdSize = dis.readInt();
        for (int i = 0; i < finalSkillIdSize; i++)
            getFinalAttackSkills().add(dis.readInt());
        return this;
    }
}
