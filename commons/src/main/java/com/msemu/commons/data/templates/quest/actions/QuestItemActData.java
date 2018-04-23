package com.msemu.commons.data.templates.quest.actions;

import com.msemu.commons.data.enums.QuestActDataType;
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
    private int itemId;

    @Getter
    @Setter
    private short quantity;

    @Getter
    @Setter
    private int prop;

    @Getter
    @Setter
    private String potentialGrade;

    @Getter
    @Setter
    private byte gender;

    @Getter
    private List<Short> jobReqs = new ArrayList<>();

    @Getter
    private List<Short> jobExReqs = new ArrayList<>();

    @Override
    public QuestActDataType getType() {
        return QuestActDataType.item;
    }

    public void setJobEx(int mask) {
        jobExReqs.addAll(getJobExFromMask(mask));
    }
    public void setJob(int mask) {
        jobReqs.addAll(getJobFromMask(mask));
    }

    private List<Short> getJobFromMask(int mask) {
        List<Short> jobs = new ArrayList<>();
        if((mask & 0x1) != 0) {
            jobs.add((short)200);
        }
        if((mask & 0x2) != 0) {
            jobs.add((short)300);
        }
        if((mask & 0x3) != 0) {
            jobs.add((short)400);
        }
        if((mask & 0x4) != 0) {
            jobs.add((short)500);
        }
        return jobs;
    }

    private List<Short> getJobExFromMask(int mask) {
        List<Short> jobs = new ArrayList<>();
        if ((mask & 0x1) != 0) {
            jobs.add((short) 0);
        }
        if ((mask & 0x2) != 0) {
            jobs.add((short) 100);
        }
        if ((mask & 0x4) != 0) {
            jobs.add((short) 200);
        }
        if ((mask & 0x8) != 0) {
            jobs.add((short) 300);
        }
        if ((mask & 0x10) != 0) {
            jobs.add((short) 400);
        }
        if ((mask & 0x20) != 0) {
            jobs.add((short) 500);
        }
        if ((mask & 0x400) != 0) {
            jobs.add((short) 1000);
        }
        if ((mask & 0x800) != 0) {
            jobs.add((short) 1100);
        }
        if ((mask & 0x1000) != 0) {
            jobs.add((short) 1100);
        }
        if ((mask & 0x2000) != 0) {
            jobs.add((short) 1300);
        }
        if ((mask & 0x4000) != 0) {
            jobs.add((short) 1400);
        }
        if ((mask & 0x8000) != 0) {
            jobs.add((short) 1500);
        }
        if ((mask & 0x20000) != 0) {
            jobs.add((short) 2001);
            jobs.add((short) 2200);
        }
        if ((mask & 0x100000) != 0) {
            jobs.add((short) 2000);
            jobs.add((short) 2001);
        }
        if ((mask & 0x200000) != 0) {
            jobs.add((short) 2100);
        }
        if ((mask & 0x400000) != 0) {
            jobs.add((short) 2001);
            jobs.add((short) 2200);
        }
        if ((mask & 0x1000000) != 0) {
            jobs.add((short) 2003);
            jobs.add((short) 2400);
        }
        if ((mask & 0x40000000) != 0) {
            jobs.add((short) 3000);
            jobs.add((short) 3200);
            jobs.add((short) 3300);
            jobs.add((short) 3500);
        }
        return jobs;
    }
}
