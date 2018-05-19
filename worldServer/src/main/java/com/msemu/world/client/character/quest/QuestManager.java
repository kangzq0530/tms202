package com.msemu.world.client.character.quest;

import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.data.templates.quest.QuestInfo;
import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.packets.outpacket.user.local.effect.LP_UserEffectLocal;
import com.msemu.core.network.packets.outpacket.user.remote.effect.LP_UserEffectRemote;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_Message;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.effect.QuestCompleteUserEffect;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.messages.QuestRecordExMessage;
import com.msemu.world.client.character.messages.QuestRecordMessage;
import com.msemu.world.client.character.quest.act.IQuestAction;
import com.msemu.world.client.character.quest.req.IQuestStartRequirements;
import com.msemu.world.client.character.quest.req.QuestProgressItemRequirement;
import com.msemu.world.client.character.quest.req.QuestStartCompletionRequirement;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.QuestData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.msemu.world.enums.ChatMsgType.YELLOW;
import static com.msemu.world.enums.QuestStatus.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@Table(name = "questManagers")
public class QuestManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "questlists")
    @MapKeyColumn(name = "questID")
    private Map<Integer, Quest> questsList = new HashMap<>();
    ;

    @Transient
    @Getter
    @Setter
    private Character character;

    public QuestManager() {

    }

    public QuestManager(Character chr) {
        this.character = chr;
    }


    public Set<Quest> getCompletedQuests() {
        return getQuestsList().entrySet().stream().filter(entry -> entry.getValue().getStatus() == COMPLETE).
                map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public Set<Quest> getQuestsInProgress() {
        return getQuestsList().entrySet().stream().filter(entry -> entry.getValue().getStatus() == STARTED).
                map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public boolean hasQuestInProgress(int questID) {
        Quest quest = getQuestsList().get(questID);
        return quest != null && quest.getStatus() == STARTED;
    }

    public boolean hasQuestCompleted(int questID) {
        Quest quest = getQuestsList().get(questID);
        return quest != null && quest.getStatus() == COMPLETE;
    }


    public int getSize() {
        return questsList.size();
    }

    public Collection<Quest> getQuests() {
        return getQuestsList().values();
    }

    public Map<Integer, Quest> getQuestsList() {
        return questsList;
    }


    /**
     * Adds a new {@link Quest} to this QuestManager's questsList. If it already exists, doesn't do anything.
     * Use {@link #replaceQuest(Quest)} if a given quest should be overridden.
     *
     * @param quest The Quest to add.
     */
    public void addQuest(Quest quest) {
        if (!getQuestsList().containsKey(quest.getQRKey())) {
            getQuestsList().put(quest.getQRKey(), quest);
            QuestInfo qi = QuestData.getInstance().getQuestInfoById(quest.getQRKey());
            getCharacter().chatMessage(YELLOW, String.format("[任務資訊] 增加任務 : %s(%d) ", qi != null ? qi.getName() : "自定義任務", quest.getQRKey()));
        }
    }

    public void startQuest(int questID) {
        Quest quest = getQuestsList().get(questID);
        QuestInfo qi = QuestData.getInstance().getQuestInfoById(questID);
        if (quest == null) {
            quest = QuestData.getInstance().createQuestFromId(questID);
            addQuest(quest);
        }
        quest.setStatus(STARTED);
        getCharacter().write(new LP_Message(new QuestRecordMessage(quest)));
        getCharacter().chatMessage(YELLOW, String.format("[任務資訊] 已接受任務 : %s(%d) ", qi != null ? qi.getName() : "自定義任務", quest.getQRKey()));
        if (qi != null && qi.isAutoComplete())
            completeQuest(questID);
    }

    /**
     * Adds a new {@link Quest} to this QuestManager's quest. If it already exists, overrides the old one with the new one.
     *
     * @param quest The Quest to add/replace.
     */
    public void replaceQuest(Quest quest) {
        getQuestsList().put(quest.getQRKey(), quest);
        getCharacter().write(new LP_Message(new QuestRecordMessage(quest)));
    }

    /**
     * Returns whether or not a {@link Character} can start a given quest.
     *
     * @param questID The Quest's ID to check.
     * @return Whether or not the Char can start the quest.
     */
    public boolean canStartQuest(int questID) {
        QuestData qd = QuestData.getInstance();
        Set<IQuestStartRequirements> questReqs = qd.getStartReqsById(questID).stream()
                .filter(qsr -> qsr instanceof QuestStartCompletionRequirement)
                .collect(Collectors.toSet());
        boolean hasQuest = questReqs.isEmpty() ||
                questReqs.stream().anyMatch(q -> q.hasRequirements(character));
        return hasQuest && qd.getStartReqsById(questID).stream()
                .allMatch(qsr -> qsr.hasRequirements(character));
    }

    /**
     * Completes a quest. Assumes the check for in-progressness has already been done, so this method can be used
     * to complete questsList that the Char does not actually have.
     *
     * @param questID The quest ID to finish.
     */
    public void completeQuest(int questID) {
        QuestData questData = QuestData.getInstance();
        QuestInfo qi = questData.getQuestInfoById(questID);
        Quest quest = getQuestsList().get(questID);
        if (quest == null) {
            quest = QuestData.getInstance().createQuestFromId(questID);
            addQuest(quest);
        }
        quest.setStatus(COMPLETE);
        quest.setCompletedTime(FileTime.getTime());
        getCharacter().chatMessage(YELLOW, String.format("[任務資訊] 已完成任務 : %s(%d) ", qi.getName(), quest.getQRKey()));
        getCharacter().write(new LP_Message(new QuestRecordMessage(quest)));
        getCharacter().write(new LP_UserEffectLocal(new QuestCompleteUserEffect()));
        getCharacter().getField().broadcastPacket(new LP_UserEffectRemote(getCharacter(), new QuestCompleteUserEffect()));
        for (QuestProgressItemRequirement qpir : quest.getItemReqs()) {
            getCharacter().consumeItem(qpir.getItemID(), qpir.getRequiredCount());
        }
        for (IQuestAction reward : questData.getCompleteActionsById(questID)) {
            reward.action(character);
        }
    }

    public void setQuestRecord(int questID, String qRValue) {
        if (!getQuestsList().containsKey(questID)) {
            addQuest(QuestData.getInstance().createQuestFromId(questID));
        }
        Quest quest = getQuestsList().get(questID);
        quest.setQrValue(qRValue);
        getCharacter().write(new LP_Message(new QuestRecordMessage(quest)));
    }

    public void setQuestRecordEx(int questID, String qRExValue) {
        if (!getQuestsList().containsKey(questID)) {
            addQuest(QuestData.getInstance().createQuestFromId(questID));
        }
        Quest quest = getQuestsList().get(questID);
        quest.setQrExValue(qRExValue);
        getCharacter().write(new LP_Message(new QuestRecordExMessage(quest)));

    }

    public void handleMobKill(Mob mob) {
        for (int questID : mob.getQuests()) {
            Quest q = getQuestsList().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleMobKill(mob.getTemplateId());
                getCharacter().write(new LP_Message(new QuestRecordMessage(q)));
            }
        }
    }

    public void handleMoneyGain(int money) {
        getQuestsInProgress().stream().filter(Quest::hasMoneyReq).forEach(q -> {
            q.addMoney(money);
            getCharacter().write(new LP_Message(new QuestRecordMessage(q)));
        });
    }

    public void handleItemGain(Item item) {
        ItemTemplate itemInfo = ItemData.getInstance().getItemInfo(item.getItemId());
        if (itemInfo == null)
            return;
        for (int questID : itemInfo.getQuestIDs()) {
            Quest q = getQuestsList().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleItemGain(item);
                getCharacter().write(new LP_Message(new QuestRecordMessage(q)));
            }
        }
    }

    public void removeQuest(int questID) {
        Quest q = getQuestsList().get(questID);
        if (q != null) {
            q.setStatus(NOT_STARTED);
            getQuestsList().remove(questID);
            getCharacter().write(new LP_Message(new QuestRecordMessage(q)));
        }
    }
}
