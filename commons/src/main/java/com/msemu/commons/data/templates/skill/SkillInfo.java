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
@Getter
@Setter
public class SkillInfo implements DatSerializable {

    private String name = "", desc = "";
    private int rootId, skillId;
    private int maxLevel, currentLevel, masterLevel, fixLevel;
    private int skillType = -1, eventTamingMob = 0, vehicleID = 0, hyper = 0, hyperStat = 0, reqLev = 0, setItemReason = 0, setItemPartsCount = 0;
    private boolean finalAttack, massSpell, invisible = false, notRemoved = false, timeLimited = false, combatOrders = false, psd = false, vSkill = false, petPassive = false;
    // info prop
    private boolean pvp = true, magicDamage, casterMove, pushTarget, pullTarget;
    private boolean keyDownSkill, summonSkill;
    private List<ReqSkill> reqSkill = new ArrayList<>();
    private Element elemAttr = Element.NEUTRAL;
    private List<Integer> psdSkills = new ArrayList<>();
    private List<Rect> rects = new ArrayList<>();
    private Map<Integer, Map<SkillStat, String>> skillStatInfo = new HashMap<>();
    private Map<Integer, Map<SkillStat, Integer>> psdWT = new HashMap<>();
    private List<Integer> finalAttackSkills = new ArrayList<>();

    public void addSkillStatInfo(int slv, SkillStat sc, String value) {
        if (!getSkillStatInfo().containsKey(slv))
            getSkillStatInfo().put(slv, new HashMap<>());
        getSkillStatInfo().get(slv).put(sc, value);
    }

    public Collection<SkillStat> getStats(int slv) {
        return getSkillStatInfo().getOrDefault(slv, new HashMap<>()).keySet();
    }

    public int getValue(SkillStat skillStat, int slv) {
        int result = 0;
        if (!getSkillStatInfo().containsKey(slv))
            return 0;
        String value = getSkillStatInfo().get(slv).get(skillStat);
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

    public void addRect(Rect rect) {
        getRects().add(rect);
    }

    public Rect getLastRect() {
        return rects != null && rects.size() > 0 ? rects.get(rects.size() - 1) : null;
    }

    public Rect getFirstRect() {
        return rects != null && rects.size() > 0 ? rects.get(0) : null;
    }

    public void addPsdSkill(int skillID) {
        getPsdSkills().add(skillID);
    }

    public void addReqSkill(ReqSkill req) {
        getReqSkill().add(req);
    }

    @Override
    public String toString() {
        return String.format("[技能] %s(%d) - %s", name, skillId, desc);
    }

    @Override
    public void write(final DataOutputStream dos) throws IOException {
        dos.writeUTF(this.name);
        dos.writeUTF(this.desc);
        dos.writeInt(this.rootId);
        dos.writeInt(this.skillId);
        dos.writeInt(this.maxLevel);
        dos.writeInt(this.currentLevel);
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
        dos.writeUTF(this.elemAttr.name());
        dos.writeInt(psdSkills.size());
        for (Integer value : psdSkills) {
            dos.writeInt(value);
        }
        dos.writeInt(rects.size());
        for (Rect rect : rects) {
            dos.writeInt(rect.getLeft());
            dos.writeInt(rect.getTop());
            dos.writeInt(rect.getRight());
            dos.writeInt(rect.getBottom());
        }
        dos.writeInt(skillStatInfo.size());
        for (Map.Entry<Integer, Map<SkillStat, String>> entry : skillStatInfo.entrySet()) {
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

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setName(dis.readUTF());
        setDesc(dis.readUTF());
        setRootId(dis.readInt());
        setSkillId(dis.readInt());
        setMaxLevel(dis.readInt());
        setCurrentLevel(dis.readInt());
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
        int reqSkilSize = dis.readInt();
        for (int i = 0; i < reqSkilSize; i++) {
            getReqSkill().add((ReqSkill) new ReqSkill().load(dis));
        }
        setElemAttr(Element.valueOf(dis.readUTF()));
        int psdSkillSize = dis.readInt();
        for (int i = 0; i < psdSkillSize; i++) {
            getPsdSkills().add(dis.readInt());
        }
        int rectsSize = dis.readInt();
        for (int i = 0; i < rectsSize; i++) {
            getRects().add(new Rect(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()));
        }
        int skillStatSize = dis.readInt();
        for (int i = 0; i < skillStatSize; i++) {
            int slv = dis.readInt();
            getSkillStatInfo().put(slv, new EnumMap<>(SkillStat.class));
            int size = dis.readInt();
            for (int j = 0; j < size; j++)
                getSkillStatInfo().get(slv).put(SkillStat.valueOf(dis.readUTF()), dis.readUTF());
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
