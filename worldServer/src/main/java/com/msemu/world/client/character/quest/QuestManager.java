package com.msemu.world.client.character.quest;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.items.Item;
import com.msemu.world.client.character.quest.requirements.IQuestStartRequirement;
import com.msemu.world.client.character.quest.requirements.QuestProgressItemRequirement;
import com.msemu.world.client.character.quest.requirements.QuestStartCompletionRequirement;
import com.msemu.world.client.character.quest.rewards.IQuestReward;
import com.msemu.world.client.life.Mob;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.QuestData;
import com.msemu.core.network.packets.out.WvsContext.messages.QuestRecordMessage;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.msemu.world.enums.ChatMsgColor.YELLOW;
import static com.msemu.world.enums.QuestStatus.COMPLETE;
import static com.msemu.world.enums.QuestStatus.NOT_STARTED;
import static com.msemu.world.enums.QuestStatus.STARTED;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@Table(name = "questManagers")
public class QuestManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "questlists")
    @MapKeyColumn(name = "questID")
    private Map<Integer, Quest> questList = new HashMap<>();;

    @Transient
    private Character character;

    public QuestManager() {
    }

    public QuestManager(Character chr) {
        this.character = chr;
    }

    public Collection<Quest> getEx() {
        return getQuests().values();
    }

    public Set<Quest> getCompletedQuests() {
        return getQuests().entrySet().stream().filter(entry -> entry.getValue().getStatus() == COMPLETE).
                map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public Set<Quest> getQuestsInProgress() {
        return getQuests().entrySet().stream().filter(entry -> entry.getValue().getStatus() == STARTED).
                map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public int getSize() {
        return questList.size();
    }

    public Map<Integer, Quest> getQuests() {
        return questList;
    }

    public boolean hasQuestInProgress(int questID) {
        Quest quest = getQuests().get(questID);
        return quest != null && quest.getStatus() == STARTED;
    }

    public boolean hasQuestCompleted(int questID) {
        Quest quest = getQuests().get(questID);
        return quest != null && quest.getStatus() == COMPLETE;
    }

    /**
     * Adds a new {@link Quest} to this QuestManager's quests. If it already exists, doesn't do anything.
     * Use {@link #replaceQuest(Quest)} if a given quest should be overridden.
     * @param quest The Quest to add.
     */
    public void addQuest(Quest quest) {
        if(!getQuests().containsKey(quest.getQRKey())) {
            getQuests().put(quest.getQRKey(), quest);
            character.write(new QuestRecordMessage(quest));
            if(quest.getStatus() == COMPLETE) {
                character.chatMessage(YELLOW, "[Info] Completed quest " + quest.getQRKey());
            } else {
                character.chatMessage(YELLOW, "[Info] Accepted quest " + quest.getQRKey());
            }
        }
    }

    /**
     * Adds a new {@link Quest} to this QuestManager's quest. If it already exists, overrides the old one with the new one.
     * @param quest The Quest to add/replace.
     */
    public void replaceQuest(Quest quest) {
        getQuests().put(quest.getQRKey(), quest);
        character.write(new QuestRecordMessage(quest));
    }

    /**
     * Returns whether or not a {@link Character} can start a given quest.
     * @param questID The Quest's ID to check.
     * @return Whether or not the Char can start the quest.
     */
    public boolean canStartQuest(int questID) {
        QuestInfo qi = QuestData.getQuestInfoById(questID);
        Set<IQuestStartRequirement> questReqs = qi.getQuestStartRequirements().stream()
                .filter(qsr -> qsr instanceof QuestStartCompletionRequirement)
                .collect(Collectors.toSet());
        boolean hasQuest = questReqs.size() == 0 ||
                questReqs.stream().anyMatch(q -> q.hasRequirements(character));
        return hasQuest && qi.getQuestStartRequirements().stream()
                .filter(qsr -> !(qsr instanceof QuestStartCompletionRequirement))
                .allMatch(qsr -> qsr.hasRequirements(character));
    }

    public Character getCharacter() {
        return character;
    }

    /**
     * Completes a quest. Assumes the check for in-progressness has already been done, so this method can be used
     * to complete quests that the Char does not actually have.
     * @param questID The quest ID to finish.
     */
    public void completeQuest(int questID) {
        QuestInfo questInfo = QuestData.getQuestInfoById(questID);
        Quest quest = getQuests().get(questID);
        if(quest == null) {
            quest = QuestData.createQuestFromId(questID);
            addQuest(quest);
        }
        quest.setStatus(COMPLETE);
        quest.setCompletedTime(FileTime.getTime());
        character.chatMessage(YELLOW, "[Info] Completed quest " + quest.getQRKey());
        character.write(new QuestRecordMessage(quest));
        for(QuestProgressItemRequirement qpir : quest.getItemReqs()) {
            character.consumeItem(qpir.getItemID(), qpir.getRequiredCount());
        }
        for(IQuestReward qr : questInfo.getQuestRewards()) {
            qr.giveReward(character);
        }
    }

    public void handleMobKill(Mob mob) {
        for(int questID : mob.getQuests()) {
            Quest q = getQuests().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleMobKill(mob.getTemplateId());
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public void handleMoneyGain(int money) {
        for(Quest q : getQuestsInProgress()) {
            if(q.hasMoneyReq()) {
                q.addMoney(money);
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public void handleItemGain(Item item) {
        if(ItemData.getItemInfoByID(item.getItemId()) == null) {
            return;
        }
        for(int questID : ItemData.getItemInfoByID(item.getItemId()).getQuestIDs()) {
            Quest q = getQuests().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleItemGain(item);
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void removeQuest(int questID) {
        Quest q = getQuests().get(questID);
        if(q != null) {
            q.setStatus(NOT_STARTED);
            getQuests().remove(questID);
            character.write(new QuestRecordMessage(q));
        }
    }
}
