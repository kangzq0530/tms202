package com.msemu.world.client.character.skills;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/11.
 */
public class LarknessInfo {
    private int rLarkness;
    private int tLarkness;
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
        outPacket.encodeInt(getrLarkness());
        outPacket.encodeInt(gettLarkness());
    }

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public int getrLarkness() {
        return rLarkness;
    }

    public void setrLarkness(int rLarkness) {
        this.rLarkness = rLarkness;
    }

    public int gettLarkness() {
        return tLarkness;
    }

    public void settLarkness(int tLarkness) {
        this.tLarkness = tLarkness;
    }
}

