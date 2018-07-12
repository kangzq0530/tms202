package com.msemu.world.client.character.party;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.townportal.LP_TownPortalCreated;
import com.msemu.core.network.packets.outpacket.townportal.LP_TownPortalRemoved;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TownPortal;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.AbstractFieldObject;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class TownPortal extends AbstractFieldObject {

    private int portalIndex;

    private int townID = 0;

    private int fieldID = 0;

    private int skillID = 0;

    private Position townPosition = new Position(0, 0);

    public TownPortal() {
    }

    public void encodeForTown(OutPacket<GameClient> outPacket, boolean isLeaving) {
        outPacket.encodeInt(isLeaving ? 999999999 : getTownID());
        outPacket.encodeInt(isLeaving ? 999999999 : getFieldID());
        if (!isLeaving) {
            outPacket.encodeInt(getSkillID());
            outPacket.encodeInt(getTownPosition().getX());
            outPacket.encodeInt(getTownPosition().getY());
        } else {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return null;
    }

    @Override
    public void enterScreen(GameClient client) {
        final int fieldId = client.getCharacter().getFieldID();
        if (getTownID() == fieldId) {
            client.write(new LP_TownPortal(this, false));
        } else if (getFieldID() == fieldId) {
            client.write(new LP_TownPortalCreated(this, false));
        }
    }

    @Override
    public void outScreen(GameClient client) {
        final int fieldId = client.getCharacter().getFieldID();
        if (getTownID() == fieldId) {
            client.write(new LP_TownPortal(this, true));
        } else if (getFieldID() == fieldId) {
            client.write(new LP_TownPortalRemoved(this, false));
        }
    }

    public void warp(Character chr, boolean fromTown) {
        if (fromTown) {
            Field toField = chr.getClient().getChannelInstance().getField(fieldID);
            chr.warp(toField);
        } else {
            Field toField = chr.getClient().getChannelInstance().getField(townID);
            chr.warp(toField);
        }
    }
}
