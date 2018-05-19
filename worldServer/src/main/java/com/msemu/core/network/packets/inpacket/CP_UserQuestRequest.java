package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.templates.quest.QuestInfo;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.client.character.quest.QuestManager;
import com.msemu.world.client.scripting.ScriptManager;
import com.msemu.world.data.QuestData;
import com.msemu.world.enums.QuestStatus;
import com.msemu.world.enums.ScriptType;

/**
 * Created by Weber on 2018/4/28.
 */
public class CP_UserQuestRequest extends InPacket<GameClient> {

    private byte type;
    private int tick = 0;
    private int questID = 0;
    private int itemID = 0;
    private int npcTemplateID = 0;
    private int selection = 0;


    public CP_UserQuestRequest(short opcode) {
        super(opcode);

    }

    @Override
    public void read() {

        type = decodeByte();
        switch (type) {
            case 0: // restore
                questID = decodeInt();
                itemID = decodeInt();
                // TODO restore item
                break;
            case 1:
                questID = decodeInt();
                npcTemplateID = decodeInt();
                break;
            case 2:
                questID = decodeInt();
                npcTemplateID = decodeInt();
                tick = decodeInt();
                if (getUnreadAmount() >= 4)
                    selection = decodeInt();
                break;
            case 3:
                questID = decodeInt();
                break;
            case 4:
                questID = decodeInt();
                npcTemplateID = decodeInt();

        }
    }

    @Override
    public void runImpl() {

        Character chr = getClient().getCharacter();
        QuestManager qm = chr.getQuestManager();

        if (questID == 0)
            return;
        QuestInfo qi = QuestData.getInstance().getQuestInfoById(questID);

        switch (type) {

            case 1: // 內建任務
                if (qm.canStartQuest(questID) && !qi.hasStartScript()) {
                    qm.startQuest(questID);
                }
                break;
            case 2: // 內建任務
                if (qm.hasQuestInProgress(questID) && !qi.hasEndScript()) {
                    Quest quest = qm.getQuestsList().get(questID);
                    if (quest.isComplete()) {
                        qm.completeQuest(questID);
                    }
                }
                break;
            case 3:
                Quest quest = qm.getQuestsList().get(questID);
                if (quest != null && quest.getStatus().equals(QuestStatus.STARTED)) {
                    qm.removeQuest(questID);
                }
                break;
            case 4:
                String startScript = qi.getStartScript();
                if (startScript.isEmpty()) {
                    startScript = String.format("%d%s%s", questID, ScriptManager.QUEST_START_SCRIPT_END_TAG, ScriptManager.SCRIPT_ENGINE_EXTENSION);
                }
                if (qm.canStartQuest(questID)) {
                    chr.getScriptManager().startScript(questID, startScript, ScriptType.QUEST);
                }
                break;
            case 5:
                if (qm.hasQuestInProgress(questID)) {
                    String endScript = qi.getEndScript();
                    if (endScript.isEmpty()) {
                        endScript = String.format("%d%s%s", questID, ScriptManager.QUEST_COMPLETE_SCRIPT_END_TAG, ScriptManager.SCRIPT_ENGINE_EXTENSION);
                    }
                    if (qi.isAutoCompleteAction())
                        qm.completeQuest(questID);
                    else
                        chr.getScriptManager().startScript(questID, endScript, ScriptType.QUEST);
                }
                break;
        }
    }
}
