package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestItemActData extends QuestActData {

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private int prop;

    @Getter
    @Setter
    private String potentialGrade;

    @Getter
    private List<Short> jobReqs = new ArrayList<>();

    @Override
    public QuestActionType getType() {
        return QuestActionType.item;
    }

    private void addJob(int job) {
        getJobReqs().add((short)job);
    }

    public void setJobReqs(int mask) {
        if ((mask & 0x1) != 0) {
            addJob(0);
        }
        if ((mask & 0x2) != 0) {
            addJob(1);
        }
        if ((mask & 0x4) != 0) {
            addJob(200);
        }
        if ((mask & 0x8) != 0) {
            addJob(300);
        }
        if ((mask & 0x10) != 0) {
            addJob(400);
        }
        if ((mask & 0x20) != 0) {
            addJob(500);
        }
        if ((mask & 0x400) != 0) {
            addJob(1000);
        }
        if ((mask & 0x800) != 0) {
            addJob(1100);
        }
        if ((mask & 0x1000) != 0) {
            addJob(1100);
        }
        if ((mask & 0x2000) != 0) {
            addJob(1300);
        }
        if ((mask & 0x4000) != 0) {
            addJob(1400);
        }
        if ((mask & 0x8000) != 0) {
            addJob(1500);
        }
        if ((mask & 0x20000) != 0) {
            addJob(2001);
            addJob(2200);
        }
        if ((mask & 0x100000) != 0) {
            addJob(2000);
            addJob(2001);
        }
        if ((mask & 0x200000) != 0) {
            addJob(2100);
        }
        if ((mask & 0x400000) != 0) {
            addJob(2001);
            addJob(2200);
        }
        if ((mask & 0x1000000) != 0) {
            addJob(2003);
            addJob(2400);
        }
        if ((mask & 0x40000000) != 0) {
            addJob(3000);
            addJob(3200);
            addJob(3300);
            addJob(3500);
        }

    }
}
