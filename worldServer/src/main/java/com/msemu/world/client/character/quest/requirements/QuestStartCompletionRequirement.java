package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.QuestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartCompletionRequirement implements IQuestStartRequirement {
    private static final Logger log = LoggerFactory.getLogger(QuestStartCompletionRequirement.class);
    private int questID;
    private byte questStatus;

    public QuestStartCompletionRequirement() {
    }

    public QuestStartCompletionRequirement(int questID, byte questStatus) {
        this.questID = questID;
        this.questStatus = questStatus;
    }

    public int getQuestID() {
        return questID;
    }

    public byte getQuestStatus() {
        return questStatus;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public void setQuestStatus(byte questStatus) {
        this.questStatus = questStatus;
    }

    @Override
    public boolean hasRequirements(Character chr) {
        QuestManager qm = chr.getQuestManager();
        switch (getQuestStatus()) {
//            case 0: // Not started
//                return !qm.hasQuestInProgress(getQuestID()) && !qm.hasQuestCompleted(getQuestID());
//            case 1: // In progress
//                return qm.hasQuestInProgress(getQuestID());
            case 0: // Completed
                return qm.hasQuestCompleted(getQuestID());
            default:
                log.error(String.format("Unknown status %d.", getQuestStatus()));
                return true;
        }
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(getQuestID());
        dos.writeByte(getQuestStatus());
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        QuestStartCompletionRequirement qscr = new QuestStartCompletionRequirement();
        qscr.setQuestID(dis.readInt());
        qscr.setQuestStatus(dis.readByte());
        return qscr;
    }
}

