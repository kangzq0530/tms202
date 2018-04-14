package com.msemu.world.network.packets.Stage;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.FieldCustom;
import com.msemu.world.constants.JobConstants;
import com.msemu.world.enums.DBChar;

/**
 * Created by Weber on 2018/4/14.
 */
public class SetField extends OutPacket {

    public SetField(Character chr, Field field, int channelId, boolean dev, int oldDriverID,
                    boolean characterData, boolean usingBuffProtector, byte portal,
                    boolean setWhiteFadeInOut, int mobStatAdjustRate, FieldCustom fieldCustom,
                    boolean canNotifyAnnouncedQuest, int stackEventGauge) {
        super(OutHeader.SetField);
        encodeInt(channelId - 1); // Damn nexon, randomly switching between starting at 1 and 0...
        encodeByte(dev);
        encodeInt(oldDriverID);
        encodeByte(characterData ? 1 : 2);
        encodeInt(0); // unused
        encodeInt(field.getWidth());
        encodeInt(field.getHeight());
        encodeByte(characterData);
        short notifierCheck = 0;
        encodeShort(notifierCheck);
        if (notifierCheck > 0) {
            encodeString(""); // pBlockReasonIter
            for (int i = 0; i < notifierCheck; i++) {
                encodeString(""); // sMsg2
            }
        }
        if (characterData) {
            // CalcDamage setSeed
            int s1 = Rand.nextInt();
            int s2 = Rand.nextInt();
            int s3 = Rand.nextInt();
            encodeInt(s1);
            encodeInt(s2);
            encodeInt(s3);
            chr.encode(this, DBChar.All); // <<<<------------------------------------
            sub_1F0E4C0();
            sub_1EA0150();
        } else {
            encodeByte(usingBuffProtector);
            encodeInt(field.getId());
            encodeByte(portal);
            encodeInt(chr.getAvatarData().getCharacterStat().getHp());
            boolean bool = false;
            encodeByte(bool);
            if (bool) {
                encodeInt(0);
                encodeInt(0);
            }
        }

        // 41 bytes below
        encodeByte(setWhiteFadeInOut);
        encodeByte(0); // unsure
        encodeFT(FileTime.getTime());
        encodeInt(mobStatAdjustRate);
        boolean hasFieldCustom = fieldCustom != null;
        encodeByte(hasFieldCustom);
        if (hasFieldCustom) {
            fieldCustom.encode(this);
        }

        encodeByte(false); // is pvp map, deprecated
        encodeByte(canNotifyAnnouncedQuest);

        encodeByte(JobConstants.isSeparatedSp(chr.getJob()));


        byte[] unkPacket = new byte[0];
        encodeInt(unkPacket.length); // 很大一包封包不之道衝沙小
        encodeArr(unkPacket);

        encodeByte(stackEventGauge >= 0);
        if (stackEventGauge >= 0) {
            encodeInt(stackEventGauge);
        }

        encodeByte(0); // Star planet, not interesting (v202:01B9FB40)
        encodeByte(0); // more star planet CWvsContext::DecodeStarPlanetRoundInfo

        // CUser::DecodeTextEquipInfo
        int size = 0;
        encodeInt(size);
        for (int i = 0; i < size; i++) {
            encodeInt(0);
            encodeString("");
        }
        // FreezeAndHotEventInfo::Decode
        encodeByte(0); // nAccountType
        encodeInt(chr.getAccId());
        // CUser::DecodeEventBestFriendInfo
        encodeInt(0); // dwEventBestFriendAID
        // unk
        encodeInt(0); // ?

        int v3_0x73B8 = -1;
        encodeInt(v3_0x73B8);

        String loginNoticePopUI = "UI/UIWindow6.img/loginNoticePopup/sundayMaple_17050";
        encodeString(loginNoticePopUI);

        byte v55 = (byte) 150;
        encodeByte(v55);

        //sub_23905D0
        int v5 = 0;
        encodeInt(v5);
        for (int i = 0; i < v5; i++) {
            encodeInt(0);
        }
    }

    private void sub_1F0E4C0() {

        int something = 0;
        encodeInt(something);
        if (something > 0) {
            do {
                //sub_612A10
                encodeInt(0);
                encodeInt(0);
                encodeInt(0);
                encodeLong(0);
                encodeLong(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);
                encodeShort(0);

                encodeInt(something);

            } while (something > 0);
        }

    }

    private void sub_1EA0150() {
        // v202 sub_1EA0150
        int idOrSomething = 0;
        encodeInt(idOrSomething);
        if (idOrSomething > 0) {
            for (int i = 0; i < 3; i++) {
                encodeInt(0);
            }
        }
    }
}
