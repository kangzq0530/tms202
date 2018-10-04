/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.outpacket.user;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarLook;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.lifes.Pet;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.constants.MapleJob;

/**
 * Created by Weber on 2018/4/12.
 */
public class LP_UserEnterField extends OutPacket<GameClient> {

    public LP_UserEnterField(Character chr) {
        super(OutHeader.LP_UserEnterField);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        AvatarLook look = chr.getAvatarData().getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        encodeInt(chr.getId());
        encodeByte(chr.getLevel());
        encodeString(chr.getName());
        // m_sParentName
        // 終極冒險加
        encodeString("");
        if (chr.getGuild() != null) {
            chr.getGuild().encodeForRemote(this);
        } else {
            Guild.defaultEncodeForRemote(this);
        }
        encodeByte(cs.getGender());
        encodeInt(cs.getPop());
        encodeInt(0); // nNameTagMark
        tsm.encodeForRemote(this);
        encodeShort(chr.getJob());
        encodeShort(cs.getSubJob());
        encodeInt(chr.getTotalChuc());
        encodeInt(0);
        look.encode(this);
        if (MapleJob.is神之子(chr.getJob())) {
            chr.getAvatarData().getZeroAvatarLook().encode(this);
        }

        unkFunction(this);


        encodeInt(chr.getDriverID());
        encodeInt(chr.getPassengerID()); // dwPassenserID

        // TODO 妮娜的魔法阵 ?

//        if(tsm.hasStat(CharacterTemporaryStat.RideVehicle) &&
//                tsm.hasStat(CharacterTemporaryStat.Flying)) {
//                addMountId(mplew, chr, buffSrc);
//                mplew.writeInt(chr.getId());
//                mplew.writeInt(0);
//        } else {
        encodeInt(0);
        encodeInt(0);
        int size = 0;
        encodeInt(size);
        for (int i = 0; i < size; i++) {
            encodeInt(0);
            encodeInt(0);
        }
//        }


        // TODO
        encodeInt(chr.getChocoCount());
        encodeInt(chr.getActiveEffectItemID());
        encodeInt(chr.getMonkeyEffectItemID());
        encodeInt(chr.getActiveNickItemID());
///
        encodeByte(false);
///
        encodeInt(chr.getDamageSkin());
        encodeInt(0); // ptPos.x?
        encodeInt(look.getDemonWingID());
        encodeInt(look.getKaiserWingID());
        encodeInt(look.getKaiserTailID());
        encodeInt(chr.getCompletedSetItemID());
        encodeShort(chr.getFieldSeatID());
        encodeString("");
        encodeString("");
        encodeShort(-1);
        encodeShort(-1);
        encodeByte(false);
        encodeInt(chr.getPortableChairID());

        // 椅子文字 m_sPortableChairMsg
        boolean hasPortableChairMsg = chr.getPortableChairMsg() != null;
        encodeInt(hasPortableChairMsg ? 1 : 0);
        if (hasPortableChairMsg) {
            encodeString(chr.getPortableChairMsg());
        }

        //
        int towerIDSize = 0;
        encodeInt(towerIDSize);
        for (int i = 0; i < towerIDSize; i++) {
            encodeInt(0); // towerChairID
        }

        // sub_81F990
        encodeByte(false);


        encodeInt(0);
        encodeInt(0);
        encodePosition(chr.getPosition());
        encodeByte(chr.getAction());
        encodeShort(chr.getFoothold());


        encodeByte(0);
        for (Pet pet : chr.getPets()) {
            if (pet.getId() == 0) {
                continue;
            }
            encodeByte(1);
            encodeInt(pet.getIdx());
            pet.encode(this);
        }
        encodeByte(0);
        encodeByte(chr.getMechanicHue());


        encodeInt(chr.getTamingMobLevel());
        encodeInt(chr.getTamingMobExp());
        encodeInt(chr.getTamingMobFatigue());
        encodeByte(false);


        encodeByte(0);

//        byte miniRoomType = chr.getMiniRoom() != null ? chr.getMiniRoom().getType() : 0;
//        encodeByte(miniRoomType);
//        if (miniRoomType > 0) {
//            //chr.getMiniRoom().encode(this);
//        }

        encodeByte(chr.getADBoardRemoteMsg() != null);
        if (chr.getADBoardRemoteMsg() != null) {
            encodeString(chr.getADBoardRemoteMsg());
        }


        encodeByte(chr.isInCouple());
        if (chr.isInCouple()) {
            chr.getCouple().encodeForRemote(this);
        }
        encodeByte(chr.getFriendshipRingRecord().size());
        if (chr.hasFriendshipItem()) {
            chr.getFriendshipRingRecord().forEach(record -> record.encode(this));
        }
        encodeByte(chr.isMarried());
        if (chr.isMarried()) {
            chr.getMarriageRecord().encodeForRemote(this);
        }

        encodeByte(0); // v65

        int m_nDelayedEffectFlag = 0;
        encodeByte(m_nDelayedEffectFlag);

        encodeInt(chr.getEvanDragonGlide());


        if (MapleJob.is凱撒(chr.getJob())) {
            encodeInt(chr.getKaiserMorphRotateHueExtern());
            encodeInt(chr.getKaiserMorphPrimiumBlack());
            encodeByte(chr.getKaiserMorphRotateHueInnner());
        }
        encodeInt(chr.getMakingMeisterSkillEff());


        for (int i = 0; i < 20; i += 4) {
            encodeByte(-1); // activeEventNameTag
        }

        encodeInt(chr.getCustomizeEffect());
        if (chr.getCustomizeEffect() > 0) {
            encodeString(chr.getCustomizeEffectMsg());
        }


        encodeByte(1);//encodeByte(chr.getSoulEffect());

        // CUser::SetFlareBlink
        // m_pFlameWizardHelper.p

//        if (tsm.hasStat(CharacterTemporaryStat.RideVehicle)) {
//            int vehicleID = tsm.getTSBByTSIndex(TSIndex.RideVehicle).getNOption();
//            if (vehicleID == 1932249) { // is_mix_vehicle
//                size = 0;
//                encodeInt(size); // ???
//                for (int i = 0; i < size; i++) {
//                    encodeInt(0);
//                }
//            }
//        }
        encodeByte(false); // Flashfire
//        boolean v90 = false;
//        mplew.write(v90);
//        if (v90) {
//            boolean v91 = false;
//            mplew.write(v91);
//            if (v91) {
//                mplew.writeInt(0);
//                mplew.writeInt(0);
//                mplew.writeShort(0);
//                mplew.writeShort(0);
//            }
//        }
        encodeByte(false); // StarPlanetRank::Decode
        encodeInt(0); // for CUser::DecodeStarPlanetTrendShopLook
        encodeInt(0); // for CUser::DecodeTextEquipInfo
        chr.getFreezeHotEventInfo().encode(this);
        encodeInt(chr.getEventBestFriendAID()); // CUser::DecodeEventBestFriendInfo

        encodeByte(tsm.hasStat(CharacterTemporaryStat.KinesisPsychicEnergeShield));
        encodeByte(chr.isBeastFormWingOn());

        encodeInt(chr.getMesoChairCount());

        encodeInt(1051291);

        encodeInt(0);
        encodeInt(0);

        encodeInt(0); // for
        encodeInt(0); // for
        encodeInt(0); // for

        encodeInt(0);

    }

    public static void unkFunction(OutPacket<GameClient> outPacket) {
        int v7 = 2;
        do {
            outPacket.encodeInt(0);
            while (true) {
                int res = 255;
                outPacket.encodeByte(res);
                if (res == 255) {
                    break;
                }
                outPacket.encodeInt(0);
            }
            v7 += 36;
        } while (v7 < 74);
    }
}
