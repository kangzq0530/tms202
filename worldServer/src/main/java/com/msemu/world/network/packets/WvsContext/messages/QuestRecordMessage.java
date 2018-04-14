package com.msemu.world.network.packets.WvsContext.messages;

import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.enums.MessageType;
import com.msemu.world.enums.QuestStatus;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestRecordMessage extends Message {

    public QuestRecordMessage(Quest quest) {
        super();
        encodeByte(MessageType.QuestRecordMessage.getValue());

        encodeInt(quest.getQRKey());
        QuestStatus status = quest.getStatus();
        encodeByte(status.getValue());

        switch (status) {
            case NOT_STARTED:
                encodeByte(0);
                break;
            case STARTED:
                encodeString(quest.getQRValue());
                break;
            case COMPLETE:
                encodeFT(quest.getCompletedTime());
                break;
        }
    }
}
