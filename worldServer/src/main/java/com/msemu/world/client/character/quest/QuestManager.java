package com.msemu.world.client.character.quest;

import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.packets.out.wvscontext.messages.QuestRecordMessage;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.quest.act.IQuestAction;
import com.msemu.world.client.character.quest.req.IQuestStartRequirements;
import com.msemu.world.client.character.quest.req.QuestProgressItemRequirement;
import com.msemu.world.client.character.quest.req.QuestStartCompletionRequirement;
import com.msemu.world.client.life.Mob;
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
            getCharacter().write(new QuestRecordMessage(quest));
            if (quest.getStatus() == COMPLETE) {
                getCharacter().chatMessage(YELLOW, "[Info] Completed quest " + quest.getQRKey());
            } else {
                getCharacter().chatMessage(YELLOW, "[Info] Accepted quest " + quest.getQRKey());
            }
        }
    }

    /**
     * Adds a new {@link Quest} to this QuestManager's quest. If it already exists, overrides the old one with the new one.
     *
     * @param quest The Quest to add/replace.
     */
    public void replaceQuest(Quest quest) {
        getQuestsList().put(quest.getQRKey(), quest);
        getCharacter().write(new QuestRecordMessage(quest));
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
        boolean hasQuest = questReqs.size() == 0 ||
                questReqs.stream().anyMatch(q -> q.hasRequirements(character));
        return hasQuest && qd.getStartReqsById(questID).stream()
                .filter(qsr -> !(qsr instanceof QuestStartCompletionRequirement))
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
        Quest quest = getQuestsList().get(questID);
        if (quest == null) {
            quest = QuestData.getInstance().createQuestFromId(questID);
            addQuest(quest);
        }
        quest.setStatus(COMPLETE);
        quest.setCompletedTime(FileTime.getTime());
        getCharacter().chatMessage(YELLOW, "[Info] Completed quest " + quest.getQRKey());
        getCharacter().write(new QuestRecordMessage(quest));
        for (QuestProgressItemRequirement qpir : quest.getItemReqs()) {
            getCharacter().consumeItem(qpir.getItemID(), qpir.getRequiredCount());
        }
        for (IQuestAction reward : questData.getCompleteActionsById(questID)) {
            reward.action(character);
        }
    }

    public void handleMobKill(Mob mob) {
        for (int questID : mob.getQuests()) {
            Quest q = getQuestsList().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleMobKill(mob.getTemplateId());
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public void handleMoneyGain(int money) {
        for (Quest q : getQuestsInProgress()) {
            if (q.hasMoneyReq()) {
                q.addMoney(money);
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public void handleItemGain(Item item) {
        ItemTemplate itemInfo = ItemData.getInstance().getItemInfo(item.getItemId());
        if (itemInfo == null)
            return;
        for (int questID : itemInfo.getQuestIDs()) {
            Quest q = getQuestsList().get(questID);
            if (q != null && !q.isComplete()) {
                q.handleItemGain(item);
                character.write(new QuestRecordMessage(q));
            }
        }
    }

    public void removeQuest(int questID) {
        Quest q = getQuestsList().get(questID);
        if (q != null) {
            q.setStatus(NOT_STARTED);
            getQuestsList().remove(questID);
            character.write(new QuestRecordMessage(q));
        }
    }
}
