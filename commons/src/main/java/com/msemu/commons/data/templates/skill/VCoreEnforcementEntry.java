package com.msemu.commons.data.templates.skill;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
public class VCoreEnforcementEntry implements DatSerializable {

    private int level;
    private int nextExp;
    private int expEnforce;
    private int extract;

    public VCoreEnforcementEntry() {
    }

    public VCoreEnforcementEntry(int level, int nextExp, int expEnforce, int extract) {
        this.level = level;
        this.nextExp = nextExp;
        this.expEnforce = expEnforce;
        this.extract = extract;
    }


    @Override
    public String toString() {
        return "[V技能強化資料] 等級：" + this.level + " 升級所需經驗：" + this.nextExp + " 強化經驗：" + this.expEnforce + " 分解碎片：" + this.extract;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(level);
        dos.writeInt(nextExp);
        dos.writeInt((expEnforce));
        dos.writeInt(extract);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.level = dis.readInt();
        this.nextExp = dis.readInt();
        this.expEnforce = dis.readInt();
        this.extract = dis.readInt();
        return this;
    }
}
