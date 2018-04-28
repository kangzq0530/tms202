package com.msemu.commons.data.templates.skill;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/23.
 */
public class CarnivalInfo implements DatSerializable {
    @Getter
    @Setter
    private List<CarnivalSkillInfo> skills = new ArrayList<>();
    @Getter
    @Setter
    private List<CarnivalGuardianInfo> guardians = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(skills.size());
        for (CarnivalSkillInfo skill : skills) {
            skill.write(dos);
        }
        dos.writeInt(guardians.size());
        for (CarnivalGuardianInfo guardian : guardians) {
            guardian.write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.getSkills().clear();
        this.getGuardians().clear();
        int skillSize = dis.readInt();
        for (int i = 0; i < skillSize; i++) {
            getSkills().add((CarnivalSkillInfo) new CarnivalSkillInfo().load(dis));
        }
        int guardianSize = dis.readInt();
        for (int i = 0; i < guardianSize; i++) {
            getGuardians().add((CarnivalGuardianInfo) (new CarnivalInfo().load(dis)));
        }
        return this;
    }
}
