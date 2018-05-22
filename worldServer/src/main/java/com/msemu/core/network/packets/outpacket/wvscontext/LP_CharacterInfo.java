package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterStat;

/**
 * Created by Weber on 2018/5/23.
 */
public class LP_CharacterInfo extends OutPacket<GameClient> {

    public LP_CharacterInfo(Character chr, boolean self) {
        super(OutHeader.LP_CharacterInfo);
        final CharacterStat cs = chr.getAvatarData().getCharacterStat();
        encodeInt(chr.getId());
        encodeByte(0);
        encodeByte(chr.getLevel());
        encodeShort(chr.getJob());
        encodeShort(chr.getSubJob());
        encodeByte(cs.getPvpGrade());
        encodeInt(cs.getPop());
        boolean hasMarriage = chr.getMarriageRecord() != null;
        encodeByte(hasMarriage);
        if (hasMarriage) {
            chr.getMarriageRecord().encode(this);
        }
        //TODO profression skill level
        encodeByte(0); // short for loop
        boolean hasGuild = chr.getGuild() != null;
        if(hasGuild) {
            encodeString(chr.getGuild().getName());
            encodeString(chr.getGuild().getName()); // TODO allaince Name
        } else {
            encodeString("");
            encodeString("");
        }

        encodeByte(self);
        encodeByte(0);
        encodeString("");
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);
        encodeByte(0);

        //tODOQQ

    }
}
