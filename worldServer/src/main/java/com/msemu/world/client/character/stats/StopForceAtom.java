package com.msemu.world.client.character.stats;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/11.
 */
public class StopForceAtom {
    @Getter
    @Setter
    private int idx;
    @Getter
    @Setter
    private int count;
    @Getter
    @Setter
    private int weaponId;
    @Getter
    private List<Integer> angleInfo = new ArrayList<>();

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getIdx());
        outPacket.encodeInt(getCount());
        outPacket.encodeInt(getWeaponId());
        outPacket.encodeInt(getAngleInfo().size());
        getAngleInfo().forEach(outPacket::encodeInt);
    }
}