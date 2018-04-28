package com.msemu.commons.data.templates.quest.reqs;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.loader.dat.DatSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/22.
 */
public class QuestPartyRankSReqData extends QuestReqData {

    final int[] partyQuests = new int[]{1200, 1201, 1202, 1203, 1204, 1205, 1206, 1300, 1301, 1302};

    @Override

    public QuestRequirementDataType getType() {
        return QuestRequirementDataType.partyQuest_S;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        return this;
    }
}
