package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.QuestInfoDatLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.quest.QuestInfo;
import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.client.character.quest.act.*;
import com.msemu.world.client.character.quest.req.*;
import com.msemu.world.enums.QuestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/12.
 */
@Reloadable(name = "quest", group = "all")
@StartupComponent("Data")
public class QuestData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(QuestData.class);
    private static final AtomicReference<QuestData> instance = new AtomicReference<>();
    @Getter(value = AccessLevel.PROTECTED)
    private final QuestInfoDatLoader questInfoDatLoader = new QuestInfoDatLoader();
    @Getter(value = AccessLevel.PROTECTED)
    private final Map<Integer, Set<IQuestAction>> questsStartActions = new HashMap<>();
    @Getter(value = AccessLevel.PROTECTED)
    private final Map<Integer, Set<IQuestAction>> questsCompleteActions = new HashMap<>();
    @Getter(value = AccessLevel.PROTECTED)
    private final Map<Integer, Set<IQuestProgressRequirement>> questsProgressRequirements = new HashMap<>();
    @Getter(value = AccessLevel.PROTECTED)
    private final Map<Integer, Set<IQuestStartRequirements>> questsStartRequirements = new HashMap<>();

    public QuestData() {
        load();
    }

    public static QuestData getInstance() {
        QuestData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new QuestData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    private void load() {
        WzManager wzManager = WorldWzManager.getInstance();
        getQuestInfoDatLoader().load();
        log.info("{} QuestInfo loaded", getQuestInfoDatLoader().getData().size());
        transformToQuestObjects();
        log.info("{} QuestStartActions loaded", getQuestsStartActions().size());
        log.info("{} QuestCompleteActions loaded", getQuestsCompleteActions().values().stream().map(Set::size).reduce(0, Integer::sum));
        log.info("{} QuestStartRequirements loaded", getQuestsStartRequirements().values().stream().map(Set::size).reduce(0, Integer::sum));
        log.info("{} QuestProgressRequirements loaded", getQuestsProgressRequirements().values().stream().map(Set::size).reduce(0, Integer::sum));
    }


    private void clear() {
        getQuestInfoDatLoader().getData().clear();
        getQuestsStartActions().clear();
        getQuestsCompleteActions().clear();
        getQuestsStartRequirements().clear();
        getQuestsProgressRequirements().clear();
    }

    private void transformActs(final int questId, Set<QuestActData> actDataList, Map<Integer, Set<IQuestAction>> actions) {
        actions.put(questId, new HashSet<>());
        actDataList.forEach(actData -> {
            IQuestAction questAction;
            switch (actData.getType()) {
                case exp:
                    questAction = new QuestExpAction();
                    break;
                case item:
                    questAction = new QuestItemAction();
                    break;
                case money:
                    questAction = new QuestMoneyAction();
                    break;
                case pop:
                    questAction = new QuestPopAction();
                    break;
                default:
                    log.warn("Unimplemented questAction: {}", actData.getType());
                    questAction = null;
                    break;
            }
            if (questAction == null)
                return;
            questAction.load(actData);
            actions.get(questId).add(questAction);
        });
    }

    private void transformToQuestObjects() {
        getQuestInfoDatLoader().getData().values().forEach(questInfo -> {
            transformActs(questInfo.getId(), questInfo.getStartActsData(), getQuestsStartActions());
            transformActs(questInfo.getId(), questInfo.getCompleteActsData(), getQuestsCompleteActions());
            transformStartReqs(questInfo.getId(), questInfo.getStartReqsData(), getQuestsStartRequirements());
            transformProgressReqs(questInfo.getId(), questInfo.getCompleteReqsData(), getQuestsProgressRequirements());
        });

    }

    private void transformProgressReqs(int questId, Set<QuestReqData> startReqsData, Map<Integer, Set<IQuestProgressRequirement>> reqs) {
        reqs.put(questId, new HashSet<>());
        startReqsData.forEach(reqData -> {
            IQuestProgressRequirement questReq;
            if (reqData == null)
                return;
            switch (reqData.getType()) {
                case item:
                    questReq = new QuestProgressItemRequirement();
                    break;
                case lvmin:
                    questReq = new QuestProgressLevelRequirement();
                    break;
                case mob:
                    questReq = new QuestProgressMobRequirement();
                    break;
                case endmeso:
                    questReq = new QuestProgressMoneyRequirement();
                    break;
                default:
                    log.warn("Unimplemented questProgressReq: {}", reqData.getType());
                    questReq = null;
                    break;
            }
            if (questReq == null)
                return;
            questReq.load(reqData);
            reqs.get(questId).add(questReq);
        });
    }

    private void transformStartReqs(int questId, Set<QuestReqData> startReqsData, Map<Integer, Set<IQuestStartRequirements>> reqs) {
        reqs.put(questId, new HashSet<>());
        startReqsData.forEach(reqData -> {
            IQuestStartRequirements questReq;
            if (reqData == null)
                return;
            switch (reqData.getType()) {
                case lvmin:
                case charismaMin:
                case charmMin:
                case craftMin:
                case insightMin:
                case senseMin:
                case willMin:

                    questReq = new QuestStartMinStatRequirement();
                    break;
                case job:
                case job_TW:
                    questReq = reqs.get(questId).stream()
                            .filter(req -> req instanceof QuestStartJobRequirement)
                            .findFirst().orElse(new QuestStartJobRequirement());
                    break;
                case lvmax:
                    questReq = new QuestStartMaxLevelRequirement();
                    break;
                case item:
                    questReq = new QuestStartItemRequirement();
                    break;
                case npc:
                    questReq = new QuestStartNpcRequirement();
                    break;
                case quest:
                    questReq = new QuestStartQuestRequirement();
                    break;
                default:
                    log.info("Unimplemented questStartReq: {}", reqData.getType());
                    questReq = null;
                    break;
            }
            if (questReq != null) {
                questReq.load(reqData);
            }
            if (!reqs.get(questId).contains(questReq)) {
                reqs.get(questId).add(questReq);
            }
        });
    }

    public Set<IQuestAction> getStartActionsById(int questId) {
        return getQuestsStartActions().get(questId);
    }

    public Set<IQuestAction> getCompleteActionsById(int questId) {
        return getQuestsCompleteActions().get(questId);
    }

    public QuestInfo getQuestInfoById(int questId) {
        return getQuestInfoDatLoader().getItem(questId);
    }

    public Quest createQuestFromId(int questID) {
        QuestInfo qi = getQuestInfoById(questID);
        Quest quest = new Quest();
        quest.setQRKey(questID);
        quest.setStatus(QuestStatus.NOT_STARTED);
        if (qi != null) {
            getQuestsProgressRequirements().get(questID).forEach(req -> quest.addQuestProgressRequirement((QuestProgressRequirement) req));
            if (qi.isAutoPreComplete())
                quest.setStatus(QuestStatus.COMPLETE);
        } else {
            // 自定義任務
        }
        return quest;
    }

    public Set<IQuestStartRequirements> getStartReqsById(int questID) {
        return getQuestsStartRequirements().get(questID);
    }

    public Set<IQuestProgressRequirement> getProgressReqById(int questID) {
        return getQuestsProgressRequirements().get(questID);
    }
}