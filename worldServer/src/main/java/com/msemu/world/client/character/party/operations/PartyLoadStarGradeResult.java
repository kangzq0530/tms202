package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.PartyResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartyLoadStarGradeResult implements IPartyResult {

    protected List<Tuple<Integer, Integer>> startGrades = new ArrayList<>();

    public PartyLoadStarGradeResult() {

    }

    public void addStartGrade(Integer characterID, Integer starGrade) {
        this.startGrades.add(new Tuple<>(characterID, starGrade));
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_Load_StarGrade_Result;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(startGrades.size());
        startGrades.forEach(sg -> {
            outPacket.encodeInt(sg.getLeft());
            outPacket.encodeInt(sg.getRight());
        });
    }
}
