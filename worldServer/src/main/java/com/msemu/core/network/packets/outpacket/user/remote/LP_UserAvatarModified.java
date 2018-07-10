package com.msemu.core.network.packets.outpacket.user.remote;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.enums.AvatarModifiedMask;

/**
 * Created by Weber on 2018/4/28.
 */
public class LP_UserAvatarModified extends OutPacket<GameClient> {

    public LP_UserAvatarModified(Character chr, byte mask, boolean carryItemEffect) {
        super(OutHeader.LP_UserAvatarModified);
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        encodeInt(chr.getId());
        encodeByte(mask);

        if ((mask & AvatarModifiedMask.AvatarLook.getVal()) != 0) {
            al.encode(this);
        }
        if ((mask & AvatarModifiedMask.SubAvatarLook.getVal()) != 0) {
            al.encode(this);
        }
        if ((mask & AvatarModifiedMask.Speed.getVal()) != 0) {
            encodeByte(tsm.getOption(CharacterTemporaryStat.Speed).nOption);
        }
        if ((mask & AvatarModifiedMask.CarryItemEffect.getVal()) != 0) {
            encodeByte(carryItemEffect);
        }
        boolean hasCouple = chr.getCouple() != null;
        encodeByte(hasCouple);
        if (hasCouple) {
            chr.getCouple().encodeForRemote(this);
        }
        encodeByte(chr.getFriendshipRingRecord().size());
        chr.getFriendshipRingRecord().forEach(ring -> ring.encode(this));
        boolean hasWedding = chr.getMarriageRecord() != null;
        encodeByte(hasWedding);
        if (hasWedding) {
            chr.getMarriageRecord().encodeForRemote(this);
        }
        encodeInt(0);
        encodeInt(chr.getCompletedSetItemID());
        encodeInt(chr.getTotalChuc());
    }

}
