package com.msemu.world.client.field.lifes;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FieldObjectType;
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
        setObjectId(objectId);
    }

    public int getActiveSkillCoolTime() {
        return -3;
    }


    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getId());
        outPacket.encodeString(getName());
        outPacket.encodeLong(getPetLockerSN());
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getAction());
        outPacket.encodeShort(getFh());
        outPacket.encodeInt(getHue());
        outPacket.encodeShort(getGiantRate());
        outPacket.encodeByte(isTransformed());
        outPacket.encodeByte(isReinforced());
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.PET;
    }

    @Override
    public void enterScreen(GameClient client) {

    }

    @Override
    public void outScreen(GameClient client) {

    }
}

