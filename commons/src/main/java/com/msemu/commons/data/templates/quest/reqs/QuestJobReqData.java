package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestJobReqData extends QuestReqData {

    @Getter
    private List<Short> jobs = new ArrayList<>();


    @Override
    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.job;
    }

    public void addJob(int job) {
        getJobs().add((short) job);
    }


}
