package com.msemu.world.client.character.skill;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
public class LarknessInfo {
    @Getter
    @Setter
    private int rLarkness;
    @Getter
    @Setter
    private int tLarkness;
    @Getter
    @Setter
    private boolean dark;

    public LarknessInfo(boolean dark) {
        this.dark = dark;
    }

    public LarknessInfo(int rLarkness, int tLarkness, boolean dark) {
        this.rLarkness = rLarkness;
        this.tLarkness = tLarkness;
        this.dark = dark;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getRLarkness());
        outPacket.encodeInt(getTLarkness());
    }
}

