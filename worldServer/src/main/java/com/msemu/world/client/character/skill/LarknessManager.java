package com.msemu.world.client.character.skill;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_IncLarknessResponse;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

public class LarknessManager {
    @Getter
    private final Character chr;
    @Getter
    private LarknessInfo darkInfo = new LarknessInfo(20040217, 0, true);
    @Getter
    private LarknessInfo lightInfo = new LarknessInfo(20040216, 0, false);
    @Getter
    @Setter
    private int darkGauge;
    @Getter
    @Setter
    private int lightGauge;
    @Getter
    @Setter
    private int darkFeathers;
    @Getter
    @Setter
    private int lightFeathers;
    @Getter
    @Setter
    private int unk;
    @Getter
    @Setter
    private boolean dark;

    public LarknessManager(Character chr) {
        this.chr = chr;

    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getDarkGauge());
        outPacket.encodeInt(getLightGauge());
//        outPacket.encodeInt(getDarkFeathers());
//        outPacket.encodeInt(getLightFeathers());
        outPacket.encodeInt(getUnk());
    }

    /**
     * Adds a dark feather, up to a maximum of 5.
     */
    private void addDarkFeather() {
        if (getDarkFeathers() < 5) {
            setDarkFeathers(getDarkFeathers() + 1);
        }
    }

    /**
     * Adds a light feather, up to a maximum of 5.
     */
    private void addLightFeather() {
        if (getLightFeathers() < 5) {
            setLightFeathers(getLightFeathers() + 1);
        }
    }

    /**
     * Adds a feather to the corresponding mode.
     *
     * @param dark mode
     */
    private void addFeather(boolean dark) {
        if (dark) {
            addDarkFeather();
        } else {
            addLightFeather();
        }
    }

    /**
     * Adds a specified amount to the given gauge. Note: Max value of a gauge is 10000.
     *
     * @param amount The amount to add to the gauge
     * @param dark   Which gauge to add the amount to
     */
    public void addGauge(int amount, boolean dark) {
        int newGauge;
        if (dark) {
            newGauge = getDarkGauge() + amount;
            if (newGauge >= 10000) {
                newGauge -= 10000;
                addDarkFeather();
            }
            setDarkGauge(newGauge);
        } else {
            newGauge = getLightGauge() + amount;
            if (newGauge >= 10000) {
                newGauge -= 10000;
                addLightFeather();
            }
            setLightGauge(newGauge);
        }
        updateInfo();
    }

    /**
     * Changes mode to dark if light, and vice versa. Includes decrementing feathers, and updating the client.
     */
    public void changeMode() {
        if (isDark() && getLightFeathers() > 0) {
            setLightFeathers(getLightFeathers() - 1);
        } else if (getDarkFeathers() > 0) {
            setDarkFeathers(getDarkFeathers() - 1);
        }
        setDark(!isDark());
        updateInfo();
    }

    /**
     * Sends a packet to update the client's larkness state
     */
    public void updateInfo() {
        chr.write(new LP_IncLarknessResponse(this));
    }
}