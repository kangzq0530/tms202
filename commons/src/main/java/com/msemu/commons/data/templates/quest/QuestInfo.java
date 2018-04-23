package com.msemu.commons.data.templates.quest;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestInfo {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private boolean autoStart = false, autoAccept = false, autoPreComplete = false, autoCancel = false,
            autoCompleteAction = false, isTimeEvent = false, blocked = false;

    @Getter
    @Setter
    private int dailyPlayTime = 0, timeLimit2 = 0, viewMedalItem = 0, medalCategory, selectedSkillID = 0;


    @Getter
    private Set<QuestActData> startActsData = new HashSet<>();

    @Getter
    private Set<QuestActData> completeActsData = new HashSet<>();

    @Getter
    Set<QuestReqData> startReqsData = new HashSet<>();

    @Getter
    Set<QuestReqData> completeReqsData = new HashSet<>();


    @Override
    public String toString() {
        return "[" + getName() + "] 自動開始: " + autoStart +
                " 自動承接: " + autoAccept +
                " 自動預先完成: " + autoPreComplete +
                " 自動取消: " + autoCancel +
                " 鎖定: " + blocked;
    }


}
