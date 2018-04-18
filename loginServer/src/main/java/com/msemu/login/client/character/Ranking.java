package com.msemu.login.client.character;

import com.msemu.commons.network.packets.OutPacket;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class Ranking {

    @Getter
    @Setter
    private int totRank;
    @Getter
    @Setter
    private int totRankGap;
    @Getter
    @Setter
    private int worldRank;
    @Getter
    @Setter
    private int worldRankGap;

    public Ranking(int totRank, int totRankGap, int worldRank, int worldRankGap) {
        this.totRank = totRank;
        this.totRankGap = totRankGap;
        this.worldRank = worldRank;
        this.worldRankGap = worldRankGap;
    }

    public Ranking() {
        this(1,3,3,7);
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getTotRank());
        outPacket.encodeInt(getTotRankGap());
        outPacket.encodeInt(getWorldRank());
        outPacket.encodeInt(getWorldRankGap());
    }
}
