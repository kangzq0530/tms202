package com.msemu.world.client.character.messages;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.enums.WvsMessageType;

/**
 * Created by Weber on 2018/5/12.
 */
public class QuestRecordExMessage implements IWvsMessage {

    private Quest quest;

    public QuestRecordExMessage(Quest quest) {
        this.quest = quest;
    }

    @Override
    public WvsMessageType getType() {
        return WvsMessageType.QuestRecordExMessage;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(quest.getQRKey());
        outPacket.encodeString(quest.getQrValue());
    }
}
