package com.msemu.world.client.life;

import com.msemu.commons.network.packets.OutPacket;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class Pet extends Life {
    private int id;
    private int idx;
    private String name;
    private long petLockerSN;
    private int hue;
    private short giantRate;
    private boolean transformed;
    private boolean reinforced;

    public Pet(int objectId) {
        super(objectId);
    }

    public int getActiveSkillCoolTime() {
        return 0;
    }


    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getId());
        outPacket.encodeString(getName());
        outPacket.encodeLong(getPetLockerSN());
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getMoveAction());
        outPacket.encodeShort(getFh());
        outPacket.encodeInt(getHue());
        outPacket.encodeShort(getGiantRate());
        outPacket.encodeByte(isTransformed());
        outPacket.encodeByte(isReinforced());

    }

}

