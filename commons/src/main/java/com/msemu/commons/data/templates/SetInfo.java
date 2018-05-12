package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/6.
 */
@Getter
@Setter
public class SetInfo implements DatSerializable{
    public int incPDD, incMDD, incSTR, incDEX, incINT, incLUK, incACC, incPAD, incMAD,
            incSpeed, incEVA, incJump, incMHP, incMMP, incMHPr, incMMPr, incAllStat;
    private List<Tuple<Integer, Integer>> activeSkills = new ArrayList<>();
    private List<Tuple<Integer, Integer>> options = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(incPDD);
        dos.writeInt(incMDD);
        dos.writeInt(incSTR);
        dos.writeInt(incDEX);
        dos.writeInt(incINT);
        dos.writeInt(incLUK);
        dos.writeInt(incACC);
        dos.writeInt(incPAD);
        dos.writeInt(incMAD);
        dos.writeInt(incSpeed);
        dos.writeInt(incEVA);
        dos.writeInt(incJump);
        dos.writeInt(incMHP);
        dos.writeInt(incMMP);
        dos.writeInt(incMHPr);
        dos.writeInt(incMMPr);
        dos.writeInt(incAllStat);
        dos.writeInt(activeSkills.size());
        for(Tuple<Integer, Integer> tup : activeSkills) {
            dos.writeInt(tup.getLeft());
            dos.writeInt(tup.getRight());
        }
        dos.writeInt(options.size());
        for(Tuple<Integer, Integer> tup : options) {
            dos.writeInt(tup.getLeft());
            dos.writeInt(tup.getRight());
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setIncPDD(dis.readInt());
        setIncMDD(dis.readInt());
        setIncSTR(dis.readInt());
        setIncDEX(dis.readInt());
        setIncINT(dis.readInt());
        setIncLUK(dis.readInt());
        setIncACC(dis.readInt());
        setIncPAD(dis.readInt());
        setIncMAD(dis.readInt());
        setIncSpeed(dis.readInt());
        setIncEVA(dis.readInt());
        setIncJump(dis.readInt());
        setIncMHP(dis.readInt());
        setIncMMP(dis.readInt());
        setIncMHPr(dis.readInt());
        setIncMMPr(dis.readInt());
        setIncAllStat(dis.readInt());
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++) {
            getActiveSkills().add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        int size2 = dis.readInt();
        for(int i = 0 ; i < size2; i++) {
            getOptions().add(new Tuple<>(dis.readInt(), dis.readInt()));
        }
        return this;
    }
}
