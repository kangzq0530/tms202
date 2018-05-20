package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/20.
 */
@Getter
@Setter
public class EquipTemplate extends ItemTemplate {
    private String title = "", iSlot = "", vSlot = "";
    private int prevBonusExpRate, attackSpeed, specialGrade, setItemID, fixedGrade;
    private short tuc, cuc, iStr, iDex, iInt, iLuk, iMaxHp, iMaxMp;
    private short iPad, iMad, iPDD, iMDD, iAcc, iEva, iCraft, iSpeed, iJump, iuc;
    private short iMaxHpR, iMaxMpR;
    private short iPvpDamage, iReduceReq, specialAttribute, durabilityMax;
    private short iIncReq, bdr, imdr, damR, statR, cuttable, exGradeOption;
    private short itemState, chuc;
    private short soulOptionId, soulSocketId, soulOption;
    private short rStr, rDex, rInt, rLuk, rLevel, rJob, rPop;
    private boolean fixedPotential, only, exItem, equipTradeBlock;
    private Map<Integer, EquipOption> options = new HashMap<>(7);


    public EquipTemplate() {
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        super.write(dos);
        dos.writeUTF(this.title);
        dos.writeUTF(this.iSlot);
        dos.writeUTF(this.vSlot);
        dos.writeInt(prevBonusExpRate);
        dos.writeInt(attackSpeed);
        dos.writeInt(specialGrade);
        dos.writeInt(setItemID);
        dos.writeInt(fixedGrade);
        dos.writeShort(tuc);
        dos.writeShort(cuc);
        dos.writeShort(iStr);
        dos.writeShort(iDex);
        dos.writeShort(iInt);
        dos.writeShort(iLuk);
        dos.writeShort(iMaxHp);
        dos.writeShort(iMaxMp);
        dos.writeShort(iMaxHpR);
        dos.writeShort(iMaxMpR);
        dos.writeShort(iPad);
        dos.writeShort(iMad);
        dos.writeShort(iPDD);
        dos.writeShort(iMDD);
        dos.writeShort(iAcc);
        dos.writeShort(iEva);
        dos.writeShort(iCraft);
        dos.writeShort(iSpeed);
        dos.writeShort(iJump);
        dos.writeShort(iuc);
        dos.writeShort(iPvpDamage);
        dos.writeShort(iReduceReq);
        dos.writeShort(specialAttribute);
        dos.writeShort(durabilityMax);
        dos.writeShort(iIncReq);
        dos.writeShort(bdr);
        dos.writeShort(imdr);
        dos.writeShort(damR);
        dos.writeShort(statR);
        dos.writeShort(cuttable);
        dos.writeShort(exGradeOption);
        dos.writeShort(itemState);
        dos.writeShort(chuc);
        dos.writeShort(soulOptionId);
        dos.writeShort(soulSocketId);
        dos.writeShort(soulOption);
        dos.writeShort(rStr);
        dos.writeShort(rDex);
        dos.writeShort(rInt);
        dos.writeShort(rLuk);
        dos.writeShort(rLevel);
        dos.writeShort(rJob);
        dos.writeShort(rPop);
        dos.writeBoolean(fixedPotential);
        dos.writeBoolean(only);
        dos.writeBoolean(exItem);
        dos.writeBoolean(equipTradeBlock);
        dos.writeInt(getOptions().size());
        for (Map.Entry<Integer, EquipOption> entry : getOptions().entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        super.load(dis);
        setTitle(dis.readUTF());
        setISlot(dis.readUTF());
        setVSlot(dis.readUTF());
        setPrevBonusExpRate(dis.readInt());
        setAttackSpeed(dis.readInt());
        setSpecialGrade(dis.readInt());
        setSetItemID(dis.readInt());
        setFixedGrade(dis.readInt());
        setTuc(dis.readShort());
        setCuc(dis.readShort());
        setIStr(dis.readShort());
        setIDex(dis.readShort());
        setIInt(dis.readShort());
        setILuk(dis.readShort());
        setIMaxHp(dis.readShort());
        setIMaxMp(dis.readShort());
        setIMaxHpR(dis.readShort());
        setIMaxMpR(dis.readShort());
        setIPad(dis.readShort());
        setIMad(dis.readShort());
        setIPDD(dis.readShort());
        setIMDD(dis.readShort());
        setIAcc(dis.readShort());
        setIEva(dis.readShort());
        setICraft(dis.readShort());
        setISpeed(dis.readShort());
        setIJump(dis.readShort());
        setIuc(dis.readShort());
        setIPvpDamage(dis.readShort());
        setIReduceReq(dis.readShort());
        setSpecialAttribute(dis.readShort());
        setDurabilityMax(dis.readShort());
        setIIncReq(dis.readShort());
        setBdr(dis.readShort());
        setImdr(dis.readShort());
        setDamR(dis.readShort());
        setStatR(dis.readShort());
        setCuttable(dis.readShort());
        setExGradeOption(dis.readShort());
        setItemState(dis.readShort());
        setChuc(dis.readShort());
        setSoulOptionId(dis.readShort());
        setSoulSocketId(dis.readShort());
        setSoulOption(dis.readShort());
        setRStr(dis.readShort());
        setRDex(dis.readShort());
        setRInt(dis.readShort());
        setRLuk(dis.readShort());
        setRLevel(dis.readShort());
        setRJob(dis.readShort());
        setRPop(dis.readShort());
        setFixedPotential(dis.readBoolean());
        setOnly(dis.readBoolean());
        setExItem(dis.readBoolean());
        setEquipTradeBlock(dis.readBoolean());
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            getOptions().put(dis.readInt(), (EquipOption) new EquipOption().load(dis));
        }
        return this;
    }
}
