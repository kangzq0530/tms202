package com.msemu.world.client.character.party.operations;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.PartyResultType;

/**
 * Created by Weber on 2018/5/4.
 */
public class PartySuccessToSelectPQRewardResponse implements IPartyResult {


    private Character character;

    private boolean remote;

    public PartySuccessToSelectPQRewardResponse(Character character, boolean remote) {
        this.character = character;
        this.remote = remote;
    }

    @Override
    public PartyResultType getType() {
        return PartyResultType.PartyRes_SuccessToSelectPQReward;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(character.getId());
        outPacket.encodeString(character.getName());
        outPacket.encodeByte(remote);
    }
}
