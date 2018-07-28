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
