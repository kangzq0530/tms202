package com.msemu.core.network.packets.outpacket.script;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.Pet;
import com.msemu.world.client.scripting.NpcScriptInfo;
import com.msemu.world.enums.NpcMessageType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/28.
 */
public class LP_ScriptMessage extends OutPacket<GameClient> {

    private void OnSay(String text, int overrideTemplateID, int param, boolean prev, boolean next, int delay) {
        if ((param & 4) != 0) {
            encodeInt(overrideTemplateID);
        }
        encodeString(text);
        encodeByte(prev);
        encodeByte(next);
        encodeInt(delay);
    }

    private void OnAskYesNo(String text, int overrideTemplateID, int param) {
        if ((param & 4) != 0) {
            encodeInt(overrideTemplateID);
        }
        encodeString(text);
    }

    private void OnSayImage(String[] images) {
        encodeByte(images.length);
        Arrays.stream(images).forEach(this::encodeString);
    }

    private void OnAskText(String text, String defText, int overrideTemplateID, int param, short lenMin, short lenMax) {
        if ((param & 4) != 0) {
            encodeInt(overrideTemplateID);
        }
        encodeString(text);
        encodeString(defText);
        encodeShort(lenMin);
        encodeShort(lenMax);
    }

    private void OnAskBoxText(String text, String defText, int overrideTemplateID, int param, short col, short line) {
        if ((param & 4) != 0) {
            encodeInt(overrideTemplateID);
        }
        encodeString(text);
        encodeString(defText);
        encodeShort(col);
        encodeShort(line);
    }

    private void OnAskBoxTextBackgrounImg(int backgroundID, String text, String defText, short col, short line, short fontSize, short fontTopMargin) {
        encodeInt(backgroundID);
        encodeString(text);
        encodeString(defText);
        encodeShort(col);
        encodeShort(line);
        encodeShort(fontSize);
        encodeShort(fontTopMargin);
    }

    private void OnAskNumber(String text, int defNumber, int min, int max) {
        encodeString(text);
        encodeInt(defNumber);
        encodeInt(min);
        encodeInt(max);
    }

    private void OnAskMenu(String text, int overrideTemplateID, int param) {
        if ((param & 4) != 0)
            encodeInt(overrideTemplateID);
        encodeString(text);
    }

    private void OnAskAvatar(boolean angelicBuster, boolean zeroBeta, String text,
                             int[] aCode, int cardID) {
        encodeByte(angelicBuster);
        encodeByte(zeroBeta);
        encodeString(text);
        encodeByte(aCode.length);
        Arrays.stream(aCode).forEach(this::encodeInt);
        encodeInt(cardID);
    }

    private void OnAskAvatarZero(String text, int gender, int[] aCode, int gender2, int[] aCode2, int cardID) {
        encodeString(text);
        encodeInt(gender);
        encodeByte(aCode.length);
        Arrays.stream(aCode).forEach(this::encodeInt);
        encodeInt(gender2);
        encodeByte(aCode2.length);
        Arrays.stream(aCode2).forEach(this::encodeInt);
        encodeInt(cardID);
    }

    private void OnAskMixHair(boolean angelicBuster, boolean zeroBeta, byte type, String text, int[] aCode, int unk) {
        encodeByte(angelicBuster);
        encodeByte(zeroBeta);
        encodeByte(type);
        encodeString(text);
        encodeByte(aCode.length);
        Arrays.stream(aCode).forEach(this::encodeInt);
        if (type == 3)
            encodeInt(unk);
        else if (type == 6)
            encodeInt(unk);
    }

    private void OnAskMixHairExZero(byte type, String text, int[] pacode, int unk, int unk2) {
        encodeByte(type);
        encodeString(text);
        encodeByte(pacode.length);
        Arrays.stream(pacode).forEach(this::encodeInt);
        encodeInt(unk);
        encodeInt(unk2);
    }

    private void OnAskCustomMixHair(boolean angelicBuster, int gender, int code, String text) {
        encodeByte(angelicBuster);
        encodeInt(gender);
        encodeInt(code);
        encodeString(text);
    }

    private void OnAskCustomMixHairAndProb(boolean angelicBuster, int gender, int code, String text) {
        encodeByte(angelicBuster);
        encodeInt(gender);
        encodeInt(code);
        encodeString(text);
    }

    private void OnAskMixHairNew(boolean angelicBuster, boolean zeroBeta, byte type,
                                 String text, byte unk, int baseColor, int mixColor, int alpha,
                                 int baseColor2, int mixColor2, int alpha2) {
        encodeByte(angelicBuster);
        encodeByte(zeroBeta);
        encodeByte(type);
        encodeString(text);
        encodeByte(unk);
        if (unk == 3) {
            encodeInt(baseColor);
            encodeInt(mixColor);
            encodeInt(alpha);
        } else if (unk == 6) {

            encodeInt(baseColor);
            encodeInt(mixColor);
            encodeInt(alpha);
            encodeInt(baseColor2);
            encodeInt(mixColor2);
            encodeInt(alpha2);
        }
    }

    private void OnAskMixHairNewExZero(byte type,
                                       String text, byte unk, int baseColor, int mixColor, int alpha,
                                       int baseColor2, int mixColor2, int alpha2, int unk2, int unk3) {
        encodeByte(type);
        encodeString(text);
        encodeByte(unk);
        encodeInt(baseColor);
        encodeInt(mixColor);
        encodeInt(alpha);
        encodeInt(baseColor2);
        encodeInt(mixColor2);
        encodeInt(alpha2);
        encodeInt(unk2);
        encodeInt(unk3);
    }

    public void OnAskAndroid(String sMsg, int[] aCode, int nCardID) {
        encodeString(sMsg); // 預覽對話介紹
        encodeByte(aCode.length); // style個數
        for (int nCode : aCode) {
            encodeInt(nCode); // style ID
        }
        encodeInt(nCardID); // Card ID
    }

    public void OnAskPet(String sMsg, Pet[] pets) {
        encodeString(sMsg);
        encodeByte(pets.length);
        for (Pet pet : pets) {
            encodeLong(pet.getId());
            encodeByte(0); // petUnk
        }
    }

    public void OnAskPetAll(String sMsg, Pet[] pets, boolean bExceptionExist) {
        encodeString(sMsg);
        encodeByte(pets.length);
        encodeByte(bExceptionExist);
        Arrays.stream(pets).forEach(pet -> {
            encodeLong(pet.getId());
            encodeByte(0);
        });
    }

    public void OnAskActionPetEvolution(String sItemID, Pet[] pets) {
        encodeString(sItemID);
        int unkVal = 11;
        if (unkVal == 11) {
            encodeByte(pets.length);
            Arrays.stream(pets).forEach(p -> encodeInt(p.getId()));
        }
    }

    public void OnInitialQuiz(boolean bUnk, String sTitle, String sProblemText, String sUnk, int nMinInput, int nMaxInput, int tRemain) {
        encodeByte(bUnk);
        if (!bUnk) {
            encodeString(sTitle);
            encodeString(sProblemText);
            encodeString(sUnk);
            encodeInt(nMinInput);
            encodeInt(nMaxInput);
            encodeInt(tRemain); // * 1000
        }
    }

    public void OnInitialSpeedQuiz(boolean bUnk, int nType, int dwAnswer, int nCorrect, int nRemain, int tRemain) {
        encodeByte(bUnk);
        if (!bUnk) {
            encodeInt(nType);
            encodeInt(dwAnswer);
            encodeInt(nCorrect);
            encodeInt(nRemain);
            encodeInt(tRemain); // * 1000
        }
    }

    public void OnICQuiz(boolean bUnk, String sQuestion, String sHint, int tRemain) {
        encodeByte(bUnk);
        if (!bUnk) {
            encodeString(sQuestion);
            encodeString(sHint);
            encodeInt(tRemain); // * 1000
        }
    }

    public void OnAskSlideMenu(int nDlgType, int nDefaultSelect, String s) {
        encodeInt(nDlgType); // 選單類型
        encodeInt(nDefaultSelect);
        encodeString(s);
    }

    public void OnAskSelectMenu(int nDlgType) {
        encodeInt(nDlgType);
        if (nDlgType == 0 || nDlgType == 1) {
            encodeInt(0); // nDefaultSelect
            int str_Size = 0;
            encodeInt(str_Size);
            for (int i = 0; i < str_Size; i++) {
                encodeString("");
            }
        }
    }

    public void OnAskAngelicBuster() {
    }

    public void OnSayIllustration(int nSpeakerTemplateID, short bParam, String sMsg, boolean bPrev, boolean bNext, int nNpcId, int nFaceIndex, int bIsLeft, int nFaceIndex2, boolean bIsDual) {
        if ((bParam & 0x4) != 0) {
            encodeInt(nSpeakerTemplateID);
        }
        encodeString(sMsg);
        encodeByte(bPrev);
        encodeByte(bNext);
        encodeInt(nNpcId);
        encodeInt(nFaceIndex);
        if (bIsDual) {
            encodeInt(bIsLeft);
            encodeInt(nFaceIndex2);
        } else {
            encodeByte(bIsLeft);
        }
    }

    public void OnAskYesNoIllustration(int nSpeakerTemplateID, short bParam, String sMsg, int nNpcId, int nFaceIndex, int bIsLeft, int nFaceIndex2, boolean bNoNpc, boolean bQuest, boolean bIsDual) {
        if ((bParam & 0x4) != 0) {
            encodeInt(nSpeakerTemplateID);
        }
        encodeString(sMsg);
        encodeInt(nNpcId);
        encodeInt(nFaceIndex);
        if (bIsDual) {
            encodeInt(bIsLeft);
            encodeInt(nFaceIndex2);
        } else {
            encodeByte(bIsLeft);
        }
    }

    public void OnAskMenuIllustration(String sMsg, int nNpcId, int nFaceIndex, int bIsLeft, int nFaceIndex2, boolean bIsDual) {
        encodeString(sMsg);
        encodeInt(nNpcId);
        encodeInt(nFaceIndex);
        if (bIsDual) {
            encodeInt(bIsLeft);
            encodeInt(nFaceIndex2);
        } else {
            encodeByte(bIsLeft);
        }
    }

    public void OnAskWeaponBox(String sMsg, int nWeaponBox, int[] aWeaponList) {
        encodeString(sMsg);
        encodeInt(nWeaponBox);
        encodeInt(aWeaponList.length);
        for (int i : aWeaponList) {
            encodeInt(i);
        }
    }

    public void OnAskUserSurvey(int nTalkType, boolean bShowExitBtn, String sTalkMsg) {
        encodeInt(nTalkType);
        encodeByte(bShowExitBtn);
        encodeString(sTalkMsg);
    }

    public void OnAskScreenShinningStarMsg() {
    }

    public void OnAskNumberUseKeyPad(int nResult) {
        encodeInt(nResult);
    }

    public void OnSpinOffGuitarRhythmGame(int nUnk, int nUnk2, int nUnk3, int nMusicNumber, String sSoundUOL) {
        encodeInt(nUnk);
        if (nUnk == 0) {
            encodeInt(nUnk2);
            encodeInt(nUnk3);
            return;
        }
        if (nUnk != 1) {
            return;
        }
        encodeInt(nMusicNumber);
        encodeString(sSoundUOL);
    }

    public void OnGhostParkEnter() {
        int size = 0;
        encodeInt(size);
        for (int i = 0; i < size; i++) {
            encodeInt(0);
            encodeInt(0); // nIncRate
            encodeInt(0); // nBonusRate
        }
    }

    public LP_ScriptMessage(NpcMessageType nmt, int nSpeakerTypeID, int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, int eColor, String[] msg, int[] value, int[][] values, Pet[] pets) {
        super(OutHeader.LP_ScriptMessage);
        encodeByte(nSpeakerTypeID);
        encodeInt(nSpeakerTemplateID);

        encodeByte(nAnotherSpeakerTemplateID > -1);
        if (nAnotherSpeakerTemplateID > -1) {
            encodeInt(nAnotherSpeakerTemplateID);
        }
        encodeByte(nmt.getValue());

        if (nOtherSpeakerTemplateID > -1) {
            bParam = bParam | 0x4;
        }
        encodeShort(bParam);
        encodeByte(eColor);
        switch (nmt) {
            case NM_SAY:
                OnSay(msg[0], nOtherSpeakerTemplateID, (short) bParam, value[0] > 0, value[1] > 0, value[2]);
                break;
            case NM_UNK_1:
                encodeString(msg[0]);
                encodeByte(value[0]);
                encodeByte(value[1]);
                encodeInt(value[2]);
                break;
            case NM_SAY_IMAGE:
                OnSayImage(msg);
                break;
            case NM_ASK_YES_NO:
                OnAskYesNo(msg[0], nOtherSpeakerTemplateID, (short) bParam);
                break;
            case NM_ASK_ACCEPT:
                OnAskYesNo(msg[0], nOtherSpeakerTemplateID, (short) bParam);
                break;
            case NM_UNK_18:
                encodeInt(value[0]);
                encodeString(msg[0]);
                break;
            case NM_ASK_TEXT:
                OnAskText(msg[0], msg[1], nOtherSpeakerTemplateID, (short) bParam, (short) value[0], (short) value[1]);
                break;
            case NM_ASK_BOX_TEXT:
                OnAskBoxText(msg[0], msg[1], nOtherSpeakerTemplateID, (short) bParam, (short) value[0], (short) value[1]);
                break;
            case NM_ASK_BOX_TEXT_BGIMG:
                OnAskBoxTextBackgrounImg((short) value[0], msg[0], msg[1], (short) value[1], (short) value[2], (short) value[3], (short) value[4]);
                break;
            case NM_ASK_NUMBER:
                OnAskNumber(msg[0], value[0], value[1], value[2]);
                break;
            case NM_ASK_MENU:
                OnAskMenu(msg[0], nOtherSpeakerTemplateID, (short) bParam);
                break;
            case NM_UNK_7:
                if ((bParam & 0x4) != 0) {
                    encodeInt(value[0]);
                }
                encodeString(msg[0]);
                encodeInt(value[1]);
                break;
            case NM_ASK_AVATAR_EX:
                OnAskAvatar(value[0] > 0, value[1] > 0, msg[0], values[0], value[2]);
                break;
            case NM_ASK_AVATAR_EX_ZERO:
                OnAskAvatarZero(msg[0], value[0], values[0], value[1], values[1], value[2]);
                break;
            case NM_ASK_MIX_HAIR:
                OnAskMixHair(value[0] > 0, value[1] > 0, (byte) value[2], msg[0], values[0], value[3]);
                break;
            case NM_ASK_MIX_HAIR_EX_ZERO:
                OnAskMixHairExZero((byte) value[0], msg[0], values[0], value[1], value[2]);
                break;
            case NM_ASK_CUSTOM_MIX_HAIR:
                OnAskCustomMixHair(value[0] > 0, value[1], value[2], msg[0]);
                break;
            case NM_ASK_CUSTOM_MIX_HAIR_AND_PROB:
                OnAskCustomMixHairAndProb(value[0] > 0, value[1], value[2], msg[0]);
                break;
            case NM_ASK_MIX_HAIR_NEW:
                OnAskMixHairNew(value[0] > 0, value[1] > 0, (byte) value[2], msg[0], (byte) value[3], value[4], value[5], value[6], value[7], value[8], value[9]);
                break;
            case NM_ASK_MIX_HAIR_NEW_EX_ZERO:
                OnAskMixHairNewExZero((byte) value[0], msg[0], (byte) value[1], value[2], value[3], value[4], value[5], value[6], value[7], value[8], value[9]);
                break;
            case NM_ASK_ANDROID:
                OnAskAndroid(msg[0], values[0], value[0]);
                break;
            case NM_ASK_PET:
                OnAskPet(msg[0], pets);
                break;
            case NM_ASK_PET_ALL:
                OnAskPetAll(msg[0], pets, value[0] > 0);
                break;
            case NM_ASK_ACTION_PET_EVOLUTION:
                OnAskActionPetEvolution(msg[0], pets);
                break;
            case NM_ASK_QUIZ:
                OnInitialQuiz(value[0] > 0, msg[0], msg[1], msg[2], value[1], value[2], value[3]);
                break;
            case NM_ASK_SPEED_QUIZ:
                OnInitialSpeedQuiz(value[0] > 0, value[1], value[2], value[3], value[4], value[5]);
                break;
            case NM_ASK_ICQ_QUIZ:
                OnICQuiz(value[0] > 0, msg[0], msg[1], value[1]);
                break;
            case NM_ASK_SLIDE_MENU:
                OnAskSlideMenu(value[0], value[1], msg[0]);
                break;
            case NM_ASK_SELECT_MENU:
                OnAskSelectMenu(value[0]);
                break;
            case NM_ASK_ANGELIC_BUSTER:
                OnAskAngelicBuster();
                break;
            case NM_SAY_ILLUSTRATION:
                OnSayIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0] > 0, value[1] > 0, value[2], value[3], value[4], value[5], false);
                break;
            case NM_SAY_DUAL_ILLUSTRATION:
                OnSayIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0] > 0, value[1] > 0, value[2], value[3], value[4], value[5], true);
                break;
            case NM_ASK_YES_NO_ILLUSTRATION:
                OnAskYesNoIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0], value[1], value[2], value[3], false, false, false);
                break;
            case NM_ASK_ACCEPT_ILLUSTRATION:
                OnAskYesNoIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0], value[1], value[2], value[3], false, true, false);
                break;
            case NM_ASK_MENU_ILLUSTRATION:
                OnAskMenuIllustration(msg[0], value[0], value[1], value[2], value[3], false);
                break;
            case NM_ASK_YES_NO_DUAL_ILLUSTRATION:
                OnAskYesNoIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0], value[1], value[2], value[3], false, false, true);
                break;
            case NM_ASK_ACCEPT_DUAL_ILLUSTRATION:
                OnAskYesNoIllustration(nOtherSpeakerTemplateID, (short) bParam, msg[0], value[0], value[1], value[2], value[3], false, true, true);
                break;
            case NM_ASK_MENU_DUAL_ILLUSTRATION:
                OnAskMenuIllustration(msg[0], value[0], value[1], value[2], value[3], true);
                break;
            case NM_ASK_WEAPON_BOX:
                OnAskWeaponBox(msg[0], value[0], values[0]);
                break;
            case NM_ASK_USER_SURVEY:
                OnAskUserSurvey(value[0], value[1] > 0, msg[0]);
                break;
            case NM_ASK_SCREEN_SHINING_START_MSG:
                OnAskScreenShinningStarMsg();
                break;
            case NM_ASK_NUMBER_KEYPAD:
                OnAskNumberUseKeyPad(value[0]);
                break;
            case NM_ASK_SPIN_OFF_GUITAR_RHYTHM_GAME:
                OnSpinOffGuitarRhythmGame(value[0], value[1], value[2], value[1], msg[0]);
                break;
            case NM_ASK_GHOST_PARTK_ENTER_UI:
                OnGhostParkEnter();
                break;
            case NM_UNK_25:
                encodeByte(value[0]);
                if (value[0] == 0) {
                    encodeString(msg[0]);
                    encodeInt(value[1]);
                    encodeInt(value[2]);
                    encodeInt(value[3]);
                    encodeInt(value[4]);
                    encodeInt(value[5]);
                }
                break;
            case NM_UNK_26:
                encodeString(msg[0]);
                encodeInt(value[0]);
                break;
            case NM_UNK_61:
            case NM_UNK_62:
                int a6;
                if ((bParam & 0x4) != 0) {
                    encodeInt(0);
                }
                if (nmt == NpcMessageType.NM_UNK_61) {
                    a6 = 0;
                } else {
                    a6 = 1;
                }
                encodeString(msg[0]);
                encodeByte(value[1]);
                encodeByte(value[2]);
                encodeInt(value[3]);
                encodeInt(value[4]);
                if (a6 == 1) {
                    encodeInt(value[5]);
                    encodeInt(value[6]);
                } else {
                    encodeByte(value[5]);
                }
                break;
            case NM_UNK_69:
                encodeString(msg[0]);
                encodeInt(value[0]);
                break;
            case NM_UNK_70:
                encodeString(msg[0]);
                encodeByte(value[0]);
                encodeByte(value[1]);
                encodeByte(value[2]);
                encodeByte(value[3]);
                encodeInt(value[4]);
                break;
            default:
                break;
        }
    }

    public LP_ScriptMessage(NpcScriptInfo nsi) {
        super(OutHeader.LP_ScriptMessage);
        encodeByte(nsi.getSpeakerType());
        encodeInt(nsi.getSpeakerTemplateID());
        int overrideTemplateID = nsi.getOverrideSpeakerTemplateID();
        encodeByte(overrideTemplateID > -1);
        if (overrideTemplateID > -1) {
            encodeInt(overrideTemplateID);
        }
        encodeByte(nsi.getLastMessageType().getValue());
        encodeShort(nsi.getParam());
        encodeByte(nsi.getColor());
        switch (nsi.getLastMessageType()) {
            case NM_SAY:
                OnSay(nsi.getText(), nsi.getOverrideSpeakerTemplateID(), nsi.getParam(), nsi.isPrev(), nsi.isNext(), nsi.getDelay());
                break;
            case NM_ASK_MENU:
                OnAskMenu(nsi.getText(), nsi.getOverrideSpeakerTemplateID(), nsi.getParam());
                break;
            case NM_ASK_YES_NO:
            case NM_ASK_ACCEPT:
                OnAskYesNo(nsi.getText(), nsi.getOverrideSpeakerTemplateID(), nsi.getParam());
                break;
            case NM_SAY_IMAGE:
                OnSayImage(nsi.getImages());
                break;
            case NM_ASK_TEXT:
                OnAskText(nsi.getText(), nsi.getDefaultText(), nsi.getOverrideSpeakerTemplateID()
                        , nsi.getParam(), (short) nsi.getMin(), (short) nsi.getMax());
                break;
            case NM_ASK_NUMBER:
                OnAskNumber(nsi.getText(), nsi.getDefaultNumber(), nsi.getMin(), nsi.getMax());
                break;

            case NM_ASK_NUMBER_KEYPAD:
                encodeInt(nsi.getResult());
                break;
            case NM_ASK_ICQ_QUIZ:
                encodeByte(nsi.getType());
                if (nsi.getType() != 1) {
                    encodeString(nsi.getText());
                    encodeString(nsi.getHintText());
                    encodeInt(nsi.getTime()); // in seconds
                }
                break;
            case NM_ASK_QUIZ:
                encodeByte(nsi.getType());
                if (nsi.getType() != 1) {
                    encodeString(nsi.getTitle());
                    encodeString(nsi.getProblemText());
                    encodeString(nsi.getHintText());
                    encodeInt(nsi.getMin());
                    encodeInt(nsi.getMax());
                    encodeInt(nsi.getTime()); // in seconds
                }
                break;
            case NM_ASK_SPEED_QUIZ:
                encodeByte(nsi.getType());
                if (nsi.getType() != 1) {
                    encodeInt(nsi.getQuizType());
                    encodeInt(nsi.getAnswer());
                    encodeInt(nsi.getCorrectAnswers());
                    encodeInt(nsi.getRemaining());
                    encodeInt(nsi.getTime()); // in seconds
                }
                break;
            case NM_ASK_BOX_TEXT:
                OnAskBoxText(nsi.getText()
                        , nsi.getDefaultText(), nsi.getOverrideSpeakerTemplateID(),
                        nsi.getParam(), nsi.getCol(), nsi.getLine());
                break;
            case NM_ASK_BOX_TEXT_BGIMG:
                OnAskBoxTextBackgrounImg(nsi.getBackgroundID(),
                        nsi.getText(), nsi.getDefaultText(),
                        nsi.getCol(), nsi.getLine(),
                        nsi.getFontSize(), nsi.getFontTopMargin());
                break;
            case NM_ASK_AVATAR_EX:
            case NM_ASK_AVATAR_EX_ZERO:
                throw new NotImplementedException();
            default:
                break;
        }
    }
}
