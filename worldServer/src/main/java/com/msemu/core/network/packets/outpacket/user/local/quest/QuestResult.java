package com.msemu.core.network.packets.outpacket.user.local.quest;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.QuestResultType;

/**
 * Created by Weber on 2018/4/29.
 */
public class QuestResult extends OutPacket<GameClient> {

    public QuestResult() {
        super(OutHeader.LP_UserQuestResult);
    }

    public QuestResult(QuestResultType type) {
        this();
        encodeByte(type.getValue());
    }

}
