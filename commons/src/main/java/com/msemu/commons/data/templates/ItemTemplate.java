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

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.enums.ItemScrollStat;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class ItemTemplate implements DatSerializable {
    protected long serialNumber;
    protected String name = "", desc = "", path = "", noFlip = "", path4Top = "";
    protected InvType invType;
    protected int itemId, price, slotMax = 1, time, stateChangeItem;
    protected int rate, reqSkillLevel, masterLevel;
    protected int bagType, charmEXP, reqQuestOnProgress, senseEXP, mobID, npcID, map;
    protected int linkedID, reqLevel, karma;
    protected int flatRate, limitMin, limitLess, sharedStatCostGrade, levelVariation, maxDays, addTime, maxLevel, minLevel, recoveryRate, recoveryHP, recoveryMP, tamingMob, dama, sitEmotion, meso, mesomin, mesomax, mesostdev, floatType, type, direction, minusLevel, skillEffectID, dressUpgrade, recover;
    protected boolean cash, tradeBlock, notSale, monsterBook, notConsume, noCursed, quest, pvpChannelLimited;
    protected boolean accountSharable, expireOnLogout, timeLimited, soldInform, purchaseShop, removeBody, randstat, blackUpgrade;
    protected Map<ItemScrollStat, Integer> scrollStats = new HashMap<>();
    protected Set<Integer> questIDs = new HashSet<>();
    protected List<Integer> skills = new ArrayList<>();

    // spec
    private ItemSpec itemSpec = new ItemSpec();

    public void putScrollStat(ItemScrollStat scrollStat, int val) {
        getScrollStats().put(scrollStat, val);
    }

    public void addQuest(int questID) {
        getQuestIDs().add(questID);
    }


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(serialNumber);
        dos.writeUTF(name);
        dos.writeUTF(desc);
        dos.writeUTF(path);
        dos.writeUTF(noFlip);
        dos.writeUTF(path4Top);
        dos.writeUTF(invType.name());
        dos.writeInt(itemId);
        dos.writeInt(price);
        dos.writeInt(slotMax);
        dos.writeInt(time);
        dos.writeInt(stateChangeItem);
        dos.writeInt(rate);
        dos.writeInt(reqSkillLevel);
        dos.writeInt(masterLevel);
        dos.writeInt(bagType);
        dos.writeInt(charmEXP);
        dos.writeInt(senseEXP);
        dos.writeInt(reqQuestOnProgress);
        dos.writeInt(mobID);
        dos.writeInt(npcID);
        dos.writeInt(map);
        dos.writeInt(linkedID);
        dos.writeInt(reqLevel);
        dos.writeInt(karma);
        dos.writeInt(flatRate);
        dos.writeInt(limitMin);
        dos.writeInt(limitLess);
        dos.writeInt(sharedStatCostGrade);
        dos.writeInt(levelVariation);
        dos.writeInt(maxDays);
        dos.writeInt(addTime);
        dos.writeInt(minLevel);
        dos.writeInt(maxLevel);
        dos.writeInt(recoveryRate);
        dos.writeInt(recoveryHP);
        dos.writeInt(recoveryMP);
        dos.writeInt(tamingMob);
        dos.writeInt(dama);
        dos.writeInt(sitEmotion);
        dos.writeInt(meso);
        dos.writeInt(mesomin);
        dos.writeInt(mesomax);
        dos.writeInt(mesostdev);
        dos.writeInt(type);
        dos.writeInt(direction);
        dos.writeInt(minusLevel);
        dos.writeInt(skillEffectID);
        dos.writeInt(dressUpgrade);
        dos.writeInt(recover);
        dos.writeBoolean(cash);
        dos.writeBoolean(tradeBlock);
        dos.writeBoolean(notSale);
        dos.writeBoolean(monsterBook);
        dos.writeBoolean(notConsume);
        dos.writeBoolean(noCursed);
        dos.writeBoolean(pvpChannelLimited);
        dos.writeBoolean(timeLimited);
        dos.writeBoolean(expireOnLogout);
        dos.writeBoolean(accountSharable);
        dos.writeBoolean(soldInform);
        dos.writeBoolean(purchaseShop);
        dos.writeBoolean(removeBody);
        dos.writeBoolean(randstat);
        dos.writeBoolean(blackUpgrade);
        dos.writeBoolean(quest);
        dos.writeInt(scrollStats.size());
        for (Map.Entry<ItemScrollStat, Integer> entry : scrollStats.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeInt(entry.getValue());
        }
        dos.writeInt(questIDs.size());
        for (Integer val : questIDs) {
            dos.writeInt(val);
        }
        dos.writeInt(skills.size());
        for (Integer val : skills) {
            dos.writeInt(val);
        }
        itemSpec.write(dos);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setSerialNumber(dis.readLong());
        setName(dis.readUTF());
        setDesc(dis.readUTF());
        setPath(dis.readUTF());
        setNoFlip(dis.readUTF());
        setPath4Top(dis.readUTF());
        setInvType(InvType.valueOf(dis.readUTF()));
        setItemId(dis.readInt());
        setPrice(dis.readInt());
        setSlotMax(dis.readInt());
        setTime(dis.readInt());
        setStateChangeItem(dis.readInt());
        setRate(dis.readInt());
        setReqSkillLevel(dis.readInt());
        setMasterLevel(dis.readInt());
        setBagType(dis.readInt());
        setCharmEXP(dis.readInt());
        setSenseEXP(dis.readInt());
        setReqQuestOnProgress(dis.readInt());
        setMobID(dis.readInt());
        setNpcID(dis.readInt());
        setMap(dis.readInt());
        setLinkedID(dis.readInt());
        setReqLevel(dis.readInt());
        setKarma(dis.readInt());
        setFlatRate(dis.readInt());
        setLimitMin(dis.readInt());
        setLimitLess(dis.readInt());
        setSharedStatCostGrade(dis.readInt());
        setLevelVariation(dis.readInt());
        setMaxDays(dis.readInt());
        setAddTime(dis.readInt());
        setMinLevel(dis.readInt());
        setMaxLevel(dis.readInt());
        setRecoveryRate(dis.readInt());
        setRecoveryHP(dis.readInt());
        setRecoveryMP(dis.readInt());
        setTamingMob(dis.readInt());
        setDama(dis.readInt());
        setSitEmotion(dis.readInt());
        setMeso(dis.readInt());
        setMesomin(dis.readInt());
        setMesomax(dis.readInt());
        setMesostdev(dis.readInt());
        setType(dis.readInt());
        setDirection(dis.readInt());
        setMinusLevel(dis.readInt());
        setSkillEffectID(dis.readInt());
        setDressUpgrade(dis.readInt());
        setRecover(dis.readInt());

        setCash(dis.readBoolean());
        setTradeBlock(dis.readBoolean());
        setNotSale(dis.readBoolean());
        setMonsterBook(dis.readBoolean());
        setNotConsume(dis.readBoolean());
        setNoCursed(dis.readBoolean());
        setPvpChannelLimited(dis.readBoolean());
        setTimeLimited(dis.readBoolean());
        setExpireOnLogout(dis.readBoolean());
        setAccountSharable(dis.readBoolean());
        setSoldInform(dis.readBoolean());
        setPurchaseShop(dis.readBoolean());
        setRemoveBody(dis.readBoolean());
        setRandstat(dis.readBoolean());
        setBlackUpgrade(dis.readBoolean());
        setQuest(dis.readBoolean());

        int scrollStatSize = dis.readInt();
        for (int i = 0; i < scrollStatSize; i++) {
            getScrollStats().put(ItemScrollStat.valueOf(dis.readUTF()), dis.readInt());
        }
        int questSize = dis.readInt();
        for (int i = 0; i < questSize; i++)
            getQuestIDs().add(dis.readInt());
        int skillSize = dis.readInt();
        for (int i = 0; i < skillSize; i++)
            getSkills().add(dis.readInt());
        itemSpec.load(dis);
        return this;
    }
}