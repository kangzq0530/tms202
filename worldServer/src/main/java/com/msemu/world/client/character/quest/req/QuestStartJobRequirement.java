package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestJobReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartJobRequirement implements IQuestStartRequirements {
    @Getter
    @Setter
    private Set<Short> jobReq;

    public QuestStartJobRequirement() {
        this.jobReq = new HashSet<>();
    }

    @Override
    public boolean hasRequirements(Character chr) {
        return getJobReq().isEmpty() || getJobReq().contains(chr.getJob());
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestJobReqData) {
            getJobReq().addAll(((QuestJobReqData) reqData).getJobs());
        }
    }
}
