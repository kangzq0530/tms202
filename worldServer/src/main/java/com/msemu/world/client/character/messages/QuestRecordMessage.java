package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.enums.QuestStatus;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/4.
 */
public class QuestRecordMessage implements IWvsMessage {

    private Quest quest;

    public QuestRecordMessage(Quest quest) {
        this.quest = quest;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.QuestRecordMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {

        outPacket.encodeInt(quest.getQRKey());
        QuestStatus status = quest.getStatus();
        outPacket.encodeByte(status.getValue());
        switch (status) {
            case NOT_STARTED:
                outPacket.encodeByte(0);
                break;
            case STARTED:
                outPacket.encodeString(quest.getQrValue());
                break;
            case COMPLETE:
                outPacket.encodeFT(quest.getCompletedTime());
                break;
        }
    }
}
