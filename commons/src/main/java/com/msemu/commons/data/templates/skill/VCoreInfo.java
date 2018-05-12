package com.msemu.commons.data.templates.skill;

import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/7.
 */
public class VCoreInfo implements DatSerializable {

    private final Map<Integer, VCoreEnforcementEntry> skill = new HashMap<>();
    private final Map<Integer, VCoreEnforcementEntry> enhance = new HashMap<>();
    private final Map<Integer, VCoreEnforcementEntry> special = new HashMap<>();

    public void addSkill(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }

    public void addEnhance(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }

    public void addSpecial(int slv, VCoreEnforcementEntry entry) {
        skill.put(slv, entry);
    }


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(skill.size());
        for(Map.Entry<Integer, VCoreEnforcementEntry> entry: skill.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.writeInt(enhance.size());
        for(Map.Entry<Integer, VCoreEnforcementEntry> entry: enhance.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.writeInt(special.size());
        for(Map.Entry<Integer, VCoreEnforcementEntry> entry: special.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        int size1 = dis.readInt();
        for(int i = 0 ; i < size1; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        int size2 = dis.readInt();
        for(int i = 0 ; i < size2; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        int size3 = dis.readInt();
        for(int i = 0 ; i < size3; i++) {
            VCoreEnforcementEntry value = new VCoreEnforcementEntry();
            special.put(dis.readInt(), (VCoreEnforcementEntry) value.load(dis));
        }
        return this;
    }
}
