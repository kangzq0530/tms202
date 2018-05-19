package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.enums.QuestActDataType;
import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.QuestInfoDatLoader;
import com.msemu.commons.data.templates.quest.QuestInfo;
import com.msemu.commons.data.templates.quest.actions.*;
import com.msemu.commons.data.templates.quest.reqs.*;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzSubProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestInfoLoader extends WzDataLoader<Map<Integer, QuestInfo>> {

    private static final Logger log = LoggerFactory.getLogger(QuestInfoLoader.class);


    private void addRequirements(Map<Integer, QuestInfo> data, WzImage checkImg) {

        checkImg.getProperties().stream()
                .map(p -> (WzSubProperty) p)
                .forEach(questProp -> {
                    QuestInfo questInfo = data.get(questProp.getInt());
                    if (questInfo == null) {
                        return;
                    }
                    questProp.getProperties().forEach(typeProp -> {
                        if (typeProp.getName().equals("0") || typeProp.getName().equals("1")) {
                            boolean isStart = typeProp.getName().equals("0");
                            ((WzSubProperty) typeProp).getProperties().forEach(prop -> {
                                String typeName = prop.getName();

                                QuestRequirementDataType type = QuestRequirementDataType.getByName(typeName);

                                QuestReqData reqData = null;

                                List<QuestReqData> questReqDatas = new LinkedList<>();


                                switch (type) {
                                    case job_TW:
                                    case job:
                                        QuestJobReqData jobReq;
                                        reqData = jobReq = new QuestJobReqData();
                                        if (prop instanceof WzSubProperty) {
                                            ((WzSubProperty) prop).getProperties().forEach(p -> jobReq.addJob(p.getShort()));
                                        }
                                        break;
                                    case item:
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            QuestItemReqData itemReq = new QuestItemReqData();
                                            if (p.getFromPath("id") == null)
                                                return;
                                            itemReq.setItemId(p.getFromPath("id").getInt());
                                            if (p.getFromPath("count") != null)
                                                itemReq.setQuantity(p.getFromPath("count").getInt());
                                            else
                                                itemReq.setQuantity(1);
                                            itemReq.setOrder(p.getFromPath("order").getInt());
                                            questReqDatas.add(itemReq);
                                        });
                                        break;
                                    case quest:
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            QuestQuestReqData questReqData = new QuestQuestReqData();
                                            if (p.getFromPath("id") == null)
                                                return;
                                            questReqData.setQuestId(p.getFromPath("id").getInt());
                                            questReqData.setState((byte) 1);
                                            if (p.getFromPath("state") != null)
                                                questReqData.setState((byte) p.getFromPath("state").getInt());
                                            questReqData.setOrder(p.getFromPath("order").getInt());
                                            questReqDatas.add(questReqData);
                                        });
                                        break;
                                    case lvmin:
                                        QuestLevelMinReqData lvminReq;
                                        reqData = lvminReq = new QuestLevelMinReqData();
                                        lvminReq.setMinLevel(prop.getInt());
                                        break;
                                    case lvmax:
                                        QuestLevelMaxReqData lvmaxReq;
                                        reqData = lvmaxReq = new QuestLevelMaxReqData();
                                        lvmaxReq.setMaxLevel(prop.getInt());
                                        break;
                                    case end:
                                    case end_t:
                                        QuestDateEndReqData dateEndReq;
                                        reqData = dateEndReq = new QuestDateEndReqData();
                                        dateEndReq.setDate(prop.getLong());
                                        break;
                                    case mob:
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            QuestMobReqData reqMob = new QuestMobReqData();
                                            reqMob.setMobId(p.getFromPath("id").getInt());
                                            reqMob.setCount((byte) p.getFromPath("count").getInt());
                                            questReqDatas.add(reqMob);
                                        });
                                        break;
                                    case npc:
                                        QuestNpcReqData npcReqData;
                                        reqData = npcReqData = new QuestNpcReqData();
                                        npcReqData.setNpcId(prop.getInt());
                                        break;
                                    case endmeso:
                                        QuestEndMesoReqData mesoReqData;
                                        reqData = mesoReqData = new QuestEndMesoReqData();
                                        mesoReqData.setMeso(prop.getInt());
                                        break;
                                    case fieldEnter:
                                        QuestFieldEnterReqData fieldReqData;
                                        reqData = fieldReqData = new QuestFieldEnterReqData();
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            fieldReqData.getFields().add(p.getLong());
                                        });
                                        break;
                                    case interval:
                                        QuestIntervalReqData interValReqData;
                                        reqData = interValReqData = new QuestIntervalReqData();
                                        interValReqData.setInterval(prop.getInt());
                                        break;
                                    case startscript:
                                        questInfo.setStartScript(prop.getString());
                                        break;
                                    case endscript:
                                        questInfo.setEndScript(prop.getString());
                                        break;
                                    case pet:
                                        QuestPetReqData petReq;
                                        reqData = petReq = new QuestPetReqData();
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            petReq.getPetItems().add(p.getInt());
                                        });
                                        break;
                                    case pettamenessmin:
                                        QuestPetTamenessMinReqData pettReq;
                                        reqData = pettReq = new QuestPetTamenessMinReqData();
                                        pettReq.setPetTamenessMin(prop.getInt());
                                        break;
                                    case mbmin:
                                        QuestMonsterBookMinReqData mbminReq;
                                        reqData = mbminReq = new QuestMonsterBookMinReqData();
                                        mbminReq.setMinMonsterCardSeen(prop.getInt());
                                        break;
                                    case skill:
                                        ((WzSubProperty) prop).getProperties().forEach(p -> {
                                            QuestSkillReqData reqSkill = new QuestSkillReqData();
                                            reqSkill.setSkillId(p.getFromPath("id").getInt());
                                            reqSkill.setLevel((byte) 1);
                                            if (p.getFromPath("level") != null)
                                                reqSkill.setLevel((byte) p.getFromPath("level").getInt());
                                            reqSkill.setLevelCondition("");
                                            if (p.getFromPath("levelCondition") != null)
                                                reqSkill.setLevelCondition(p.getFromPath("levelCondition").getString());
                                            reqSkill.setAcquire(false);
                                            if (p.getFromPath("acquire") != null)
                                                reqSkill.setAcquire(p.getFromPath("acquire").getInt() > 0);
                                            questReqDatas.add(reqSkill);
                                        });
                                        break;
                                    case pop:
                                        QuestPopReqData popReqData;
                                        reqData = popReqData = new QuestPopReqData();
                                        popReqData.setPop(prop.getInt());
                                        break;
                                    case subJobFlags:
                                        QuestSubJobFlagReqData subJobReq;
                                        reqData = subJobReq = new QuestSubJobFlagReqData();
                                        subJobReq.setSubJobFlag(prop.getInt());
                                        break;
                                    case partyQuest_S:
                                        break;
//                                    case normalAutoStart:
//                                        QuestNormalStartReqData normalStart;
//                                        reqData = normalStart = new QuestNormalStartReqData();
//                                        break;
                                    case dayByDay:
                                        break;
                                    case questComplete:
                                        break;
                                    case charmMin:
                                    case senseMin:
                                    case insightMin:
                                    case craftMin:
                                    case willMin:
                                    case charismaMin:
                                        break;

                                }
                                if (!questReqDatas.contains(reqData) && reqData != null)
                                    questReqDatas.add(reqData);
                                if (isStart) {
                                    questReqDatas.forEach(p -> questInfo.getStartReqsData().add(p));
                                } else {
                                    questReqDatas.forEach(p -> questInfo.getCompleteReqsData().add(p));
                                }

                                if (questReqDatas.size() > 5) {
                                    int x = 1;
                                }

                                questReqDatas.clear();
                            });

                        }
                    });
                });
    }


    @Override
    public Map<Integer, QuestInfo> load(WzManager wzManager) {
        WzDirectory wzDirectory = wzManager.getWz(WzManager.QUEST).getWzDirectory();

        Map<Integer, QuestInfo> data = new HashMap<>();


        WzImage questInfoImg = wzDirectory.getImage("QuestInfo.img");

        questInfoImg.getProperties().stream()
                .map(p -> (WzSubProperty) p)
                .forEach(questProp -> {
                    QuestInfo questInfo = new QuestInfo();
                    questProp.getProperties().forEach(prop -> {
                        if (prop.getName().equals("autoStart")) {
                            questInfo.setName(prop.getString());
                        } else if (prop.getName().equals("autoComplete")) {
                            questInfo.setAutoComplete(prop.getInt() > 0);
                        } else if (prop.getName().equals("autoStart")) {
                            questInfo.setAutoStart(prop.getInt() > 0);

                        } else if (prop.getName().equals("selfStart")) {
                            questInfo.setSelfStart(prop.getInt() > 0);
                        } else if (prop.getName().equals("autoAccept")) {
                            questInfo.setAutoAccept(prop.getInt() > 0);
                        } else if (prop.getName().equals("autoPreComplete")) {
                            questInfo.setAutoPreComplete(prop.getInt() > 0);
                        } else if (prop.getName().equals("autoCancel")) {
                            questInfo.setAutoCancel(prop.getInt() > 0);
                        } else if (prop.getName().equals("autoCompleteAction")) {
                            questInfo.setAutoCompleteAction(prop.getInt() > 0);
                        } else if (prop.getName().equals("isTimeEvent")) {
                            questInfo.setTimeEvent(prop.getInt() > 0);
                        } else if (prop.getName().equals("name")) {
                            questInfo.setName(prop.getString());
                        } else if (prop.getName().equals("selectedSkillID")) {
                            questInfo.setSelectedSkillID(prop.getInt());
                        } else if (prop.getName().equals("medalCategory")) {
                            questInfo.setMedalCategory(prop.getInt());
                        } else if (prop.getName().equals("viewMedalItem")) {
                            questInfo.setViewMedalItem(prop.getInt());
                        } else if (prop.getName().equals("timeLimit2")) {
                            questInfo.setTimeLimit2(prop.getInt());
                        } else if (prop.getName().equals("dailyPlayTime")) {
                            questInfo.setDailyPlayTime(prop.getInt());
                        }
                    });
                    questInfo.setId(questProp.getInt());
                    data.put(questInfo.getId(), questInfo);

                });

        addRequirements(data, wzDirectory.getImage("Check.img"));
        addActions(data, wzDirectory.getImage("Act.img"));

        return data;

    }

    private void addActions(Map<Integer, QuestInfo> data, WzImage image) {
        image.getProperties().stream().map(e -> (WzSubProperty) e)
                .forEach(questProp -> {
                    QuestInfo questInfo = data.get(questProp.getInt());
                    if (questInfo == null) {
                        return;
                    }
                    questProp.getProperties().stream()
                            .filter(e -> e instanceof WzSubProperty && (e.getName().equals("0") || e.getName().equals("1")))
                            .map(e -> (WzSubProperty) e)
                            .forEach(typeProp -> {

                                boolean isStart = typeProp.getName().equals("0");

                                typeProp.getProperties().forEach(prop -> {
                                    QuestActData actData = null;
                                    List<QuestActData> actdataList = new ArrayList<>();
                                    String typeName = prop.getName();
                                    QuestActDataType type = QuestActDataType.getQuestActionDataByName(typeName);
                                    switch (type) {
                                        case exp:
                                            QuestExpActData expAct;
                                            actData = expAct = new QuestExpActData();
                                            expAct.setExp(prop.getLong());
                                            break;
                                        case item:
                                            ((WzSubProperty) prop).getProperties().forEach(subProp -> {
                                                if (subProp.getFromPath("id") == null)
                                                    return;
                                                QuestItemActData itemAct = new QuestItemActData();
                                                itemAct.setItemId(subProp.getFromPath("id").getInt());
                                                itemAct.setQuantity((short) 1);
                                                if (subProp.getFromPath("count") != null)
                                                    itemAct.setQuantity(subProp.getFromPath("count").getShort());
                                                if (subProp.getFromPath("potentialGrade") != null)
                                                    itemAct.setPotentialGrade(subProp.getFromPath("potentialGrade").getString());
                                                if (subProp.getFromPath("job") != null) {
                                                    itemAct.setJob(subProp.getFromPath("job").getInt());
                                                }
                                                if (subProp.getFromPath("jobEx") != null) {
                                                    itemAct.setJobEx((subProp.getFromPath("jobEx").getInt()));
                                                }
                                                actdataList.add(itemAct);
                                            });
                                            break;
                                        case money:
                                            QuestMoneyActData moneyAct;
                                            actData = moneyAct = new QuestMoneyActData();
                                            moneyAct.setMoney(prop.getInt());
                                            break;
                                        case quest:
                                            ((WzSubProperty) prop).getProperties().stream()
                                                    .map(e -> (WzSubProperty) e)
                                                    .forEach(p -> {
                                                        QuestQuestActData questActData = new QuestQuestActData();

                                                        if (p.hasProperty("id")) {
                                                            questActData.setQuest(p.getInt());
                                                        }
                                                        if (p.hasProperty("state")) {
                                                            questActData.setState((byte) p.getInt());
                                                        }
                                                        actdataList.add(questActData);
                                                    });
                                            break;
                                        case buffItemID:
                                            QuestBuffItemActData buffItemActData;
                                            actData = buffItemActData = new QuestBuffItemActData();
                                            buffItemActData.setBuffItemID(prop.getInt());
                                            break;
                                        case willEXP:
                                            QuestWillEXPActData willEXPActionData;
                                            actData = willEXPActionData = new QuestWillEXPActData();
                                            willEXPActionData.setWillEXP(prop.getInt());
                                            break;
                                        case charismaEXP:
                                            QuestCharismaEXPActData questCharismaEXPActData;
                                            actData = questCharismaEXPActData = new QuestCharismaEXPActData();
                                            questCharismaEXPActData.setCharismaEXP(prop.getInt());
                                            break;
                                        case charmEXP:
                                            QuestCharmEXPActData charmEXPActionData;
                                            actData = charmEXPActionData = new QuestCharmEXPActData();
                                            charmEXPActionData.setCharmEXP(prop.getInt());
                                            break;
                                        case craftEXP:
                                            QuestCraftEXPActData craftEXPActData;
                                            actData = craftEXPActData = new QuestCraftEXPActData();
                                            craftEXPActData.setCraftEXP(prop.getInt());
                                            break;
                                        case nextQuest:
                                            QuestNextQuestActData nextQuestActData;
                                            actData = nextQuestActData = new QuestNextQuestActData();
                                            nextQuestActData.setNextQuest(prop.getInt());
                                            break;
                                        case pop:
                                            QuestPopActionData popActionData;
                                            actData = popActionData = new QuestPopActionData();
                                            popActionData.setPop(prop.getInt());
                                            break;
                                        case senseEXP:
                                            QuestSenseEXPActData senseEXPActionData;
                                            actData = senseEXPActionData = new QuestSenseEXPActData();
                                            senseEXPActionData.setSenseEXP(prop.getInt());
                                            break;
                                        case sp:
                                            QuestSpActData spActionData;
                                            actData = spActionData = new QuestSpActData();
                                            if (prop.hasProperty("0/sp_value")) {
                                                spActionData.setSp(prop.getFromPath("0/sp_value").getShort());
                                            }
                                            if (prop.hasProperty("0/job")) {
                                                ((WzSubProperty) prop.getFromPath("0/job")).getProperties()
                                                        .forEach(p -> {
                                                            spActionData.getJobs().add(p.getShort());
                                                        });
                                                spActionData.setSp(prop.getFromPath("0/sp_value").getShort());
                                            }
                                            break;
                                        case transferField:
                                            QuestTransferFieldActData transferFieldActData;
                                            actData = transferFieldActData = new QuestTransferFieldActData();
                                            transferFieldActData.setTransferField(prop.getInt());
                                            if (prop.getFromPath("../portalName") != null) {
                                                transferFieldActData.setPortalName(prop.getFromPath("../portalName").getString());
                                            }
                                            break;
                                        case skill:
                                            QuestSkillActData skillActData;
                                            actData = skillActData = new QuestSkillActData();
                                            if (prop.hasProperty("id")) {
                                                skillActData.setSkill(prop.getFromPath("id").getInt());
                                            }
                                            if (prop.hasProperty("skillLevel")) {
                                                skillActData.setLevel(prop.getFromPath("skillLevel").getInt());
                                            }
                                            if (prop.hasProperty("masterLevel")) {
                                                skillActData.setMasterLevel(prop.getFromPath("masterLevel").getInt());
                                            }
                                            if (prop.hasProperty("job")) {
                                                ((WzSubProperty) prop.getFromPath("job")).getProperties()
                                                        .forEach(p -> {
                                                            skillActData.getJobs().add(p.getShort());
                                                        });
                                            }
                                            break;

                                    }

                                    if (actData != null)
                                        actdataList.add(actData);

                                    if (isStart) {
                                        actdataList.forEach(e -> questInfo.getStartActsData().add(e));
                                    } else {
                                        actdataList.forEach(e -> questInfo.getCompleteActsData().add(e));
                                    }

                                    actdataList.clear();

                                });
                            });
                });
    }


    @Override
    public void saveToDat(WzManager wzManager) throws IOException {
        new QuestInfoDatLoader().saveDat(load(wzManager));
    }
}
