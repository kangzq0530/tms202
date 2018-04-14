package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;

/**
 * Created by Weber on 2018/4/13.
 */
public class TownPortal {

    private int townID;

    private int fieldID;

    private int skillID;

    private Position position;


    public TownPortal() {

    }

    public int getTownID() {
        return townID;
    }

    public void setTownID(int townID) {
        this.townID = townID;
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getTownID());
        outPacket.encodeInt(getFieldID());
        outPacket.encodeInt(getSkillID());
        outPacket.encodePosition(getPosition());
    }
}
