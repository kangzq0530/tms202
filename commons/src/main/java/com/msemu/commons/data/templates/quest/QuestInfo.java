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

package com.msemu.commons.data.templates.quest;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/22.
 */
@Getter
@Setter
public class QuestInfo implements DatSerializable {
    private String name = "";
    private String startScript = "", endScript = "";
    private int id;
    private boolean autoStart = false, selfStart = false, autoAccept = false, autoComplete = false, autoPreComplete = false, autoCancel = false,
            autoCompleteAction = false, isTimeEvent = false, blocked = false;
    private int dailyPlayTime = 0, timeLimit2 = 0, viewMedalItem = 0, medalCategory, selectedSkillID = 0;
    private Set<QuestActData> startActsData = new HashSet<>();
    private Set<QuestActData> completeActsData = new HashSet<>();
    private Set<QuestReqData> startReqsData = new HashSet<>();
    private Set<QuestReqData> completeReqsData = new HashSet<>();

    @Override
    public String toString() {
        return "[" + getName() + "] 自動開始: " + autoStart +
                " 自動承接: " + autoAccept +
                " 自動預先完成: " + autoPreComplete +
                " 自動取消: " + autoCancel +
                " 鎖定: " + blocked;
    }

    public boolean hasStartScript() {
        return !startScript.isEmpty();
    }

    public boolean hasEndScript() {
        return !endScript.isEmpty();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(this.name);
        dos.writeUTF(this.startScript);
        dos.writeUTF(this.endScript);
        dos.writeInt(this.id);
        dos.writeBoolean(this.autoStart);
        dos.writeBoolean(this.selfStart);
        dos.writeBoolean(this.autoComplete);
        dos.writeBoolean(this.autoAccept);
        dos.writeBoolean(this.autoPreComplete);
        dos.writeBoolean(this.autoCancel);
        dos.writeBoolean(this.autoCompleteAction);
        dos.writeBoolean(this.isTimeEvent);
        dos.writeBoolean(this.blocked);
        dos.writeInt(this.dailyPlayTime);
        dos.writeInt(this.timeLimit2);
        dos.writeInt(this.viewMedalItem);
        dos.writeInt(this.medalCategory);
        dos.writeInt(this.selectedSkillID);
        dos.writeInt(this.getStartActsData().size());
        for (QuestActData actData : getStartActsData()) {
            String clazzName = actData.getClass().getSimpleName();
            dos.writeUTF(clazzName);
            actData.write(dos);
        }
        dos.writeInt(this.getCompleteActsData().size());
        for (QuestActData actData : getCompleteActsData()) {
            String clazzName = actData.getClass().getSimpleName();
            dos.writeUTF(clazzName);
            actData.write(dos);
        }
        dos.writeInt(this.getStartReqsData().size());
        for (QuestReqData reqData : getStartReqsData()) {
            if (reqData.getClass() == null) {

            }
            String clazzName = reqData.getClass().getSimpleName();
            dos.writeUTF(clazzName);
            reqData.write(dos);
        }
        dos.writeInt(this.getCompleteReqsData().size());
        for (QuestReqData reqData : getCompleteReqsData()) {
            String clazzName = reqData.getClass().getSimpleName();
            dos.writeUTF(clazzName);
            reqData.write(dos);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setName(dis.readUTF());
        this.setStartScript(dis.readUTF());
        this.setEndScript(dis.readUTF());
        this.setId(dis.readInt());
        this.setAutoStart(dis.readBoolean());
        this.setSelfStart(dis.readBoolean());
        this.setAutoComplete(dis.readBoolean());
        this.setAutoAccept(dis.readBoolean());
        this.setAutoPreComplete(dis.readBoolean());
        this.setAutoCancel(dis.readBoolean());
        this.setAutoCompleteAction(dis.readBoolean());
        this.setTimeEvent(dis.readBoolean());
        this.setBlocked(dis.readBoolean());
        this.setDailyPlayTime(dis.readInt());
        this.setTimeLimit2(dis.readInt());
        this.setViewMedalItem(dis.readInt());
        this.setMedalCategory(dis.readInt());
        this.setSelectedSkillID(dis.readInt());

        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String clazzName = dis.readUTF();
            try {
                Class<?> clazz = Class.forName("com.msemu.commons.data.templates.quest.actions." + clazzName);
                QuestActData actData = (QuestActData) clazz.newInstance();
                actData.load(dis);
                getStartActsData().add(actData);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }


        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String clazzName = dis.readUTF();
            try {
                Class<?> clazz = Class.forName("com.msemu.commons.data.templates.quest.actions." + clazzName);
                QuestActData actData = (QuestActData) clazz.newInstance();
                actData.load(dis);
                getCompleteActsData().add(actData);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String clazzName = dis.readUTF();
            try {
                Class<?> clazz = Class.forName("com.msemu.commons.data.templates.quest.reqs." + clazzName);
                QuestReqData reqData = (QuestReqData) clazz.newInstance();
                reqData.load(dis);
                getStartReqsData().add(reqData);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String clazzName = dis.readUTF();
            try {
                Class<?> clazz = Class.forName("com.msemu.commons.data.templates.quest.reqs." + clazzName);
                QuestReqData reqData = (QuestReqData) clazz.newInstance();
                reqData.load(dis);
                getCompleteReqsData().add(reqData);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return this;
    }
}
