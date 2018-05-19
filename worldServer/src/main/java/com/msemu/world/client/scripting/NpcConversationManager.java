package com.msemu.world.client.scripting;

import com.msemu.core.network.packets.outpacket.script.LP_SayScriptMessage;
import com.msemu.core.network.packets.outpacket.script.LP_ScriptMessage;
import com.msemu.core.network.packets.outpacket.script.LP_SelfTalkScriptMessage;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.NpcMessageParam;
import com.msemu.world.enums.NpcMessageType;
import lombok.Getter;

/**
 * Created by Weber on 2018/5/18.
 */
public class NpcConversationManager extends AbstractInteractionManager {

    @Getter
    private NpcScriptInfo npcScriptInfo;


    public NpcConversationManager(Character character, int templateID) {
        super(character, templateID);
        this.npcScriptInfo = new NpcScriptInfo();
    }

    public void say(String sMsg) {
        say(sMsg, false, false);
    }

    public void sayWithoutClosedButton(String sMsg) {
        say(NpcMessageParam.NO_CLOSE.getValue(), sMsg, false, false);
    }

    public void sayOnRightSide(String sMsg) {
        say(NpcMessageParam.SHOW_CHAR_ON_RIGHT_SIDE.getValue(), sMsg, false, false);
    }

    public void sayOnRightSideFaceRight(String sMsg) {
        say(NpcMessageParam.SHOW_CHAR_ON_RIGHT_SIDE.getValue() | NpcMessageParam.CHAR_ON_RIGHT_SIDE_TURN_RIGHT.getValue(), sMsg, false, false);
    }

    public void sayOverrideOn(String sMsg) {
        say(NpcMessageParam.SHOW_CHAR_ON_RIGHT_SIDE.getValue() | NpcMessageParam.CHAR_ON_RIGHT_SIDE_TURN_RIGHT.getValue(), sMsg, false, false);
    }


    public void sayNext(String sMsg) {
        say(sMsg, false, true);
    }

    public void sayPrev(String sMsg) {
        say(sMsg, true, false);
    }

    public void sayPrevNext(String sMsg) {
        say(sMsg, true, true);
    }


    public void say(String sMsg, boolean prev, boolean next) {
        say(0, sMsg, prev, next);
    }

    public void sayNext(int bParam, String sMsg) {
        say(bParam, sMsg, false, true);
    }

    public void sayPrev(int bParam, String sMsg) {
        say(bParam, sMsg, true, false);
    }

    public void sayPrevNext(int bParam, String sMsg) {
        say(bParam, sMsg, true, true);
    }

    public void say(int bParam, String sMsg, boolean prev, boolean next) {
        say(getTemplateID(), bParam, sMsg, prev, next);
    }

    public void sayNext(int nSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, -1, bParam, sMsg, false, true);
    }

    public void sayPrev(int nSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, -1, bParam, sMsg, true, false);
    }

    public void sayPrevNext(int nSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, -1, bParam, sMsg, true, true);
    }


    public void say(int nSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(nSpeakerTemplateID, nSpeakerTemplateID, bParam, sMsg, prev, next);
    }

    public void say(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg, prev, next);
    }

    public void sayPrev(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg, true, false);
    }

    public void sayNext(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg, false, true);
    }

    public void sayPrevNext(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg) {
        say(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg, true, true);
    }

    public void say(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg, prev, next, 0);
    }

    public void sayPrev(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg) {
        say(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg, true, false, 0);
    }

    public void sayNext(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg) {
        say(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg, false, true, 0);
    }

    public void sayPrevNext(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg) {
        say(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg, true, true, 0);
    }

    public void say(int speakerTypeID, int speakerTemplateID, int anotherSpeakerTemplateID, int overrideSpeakerTemplateID, int bParam, int eColor, String sMsg, boolean prev, boolean next, int delay) {
        if (sMsg.contains("#L")) {
            askMenu(speakerTypeID, speakerTemplateID, anotherSpeakerTemplateID, overrideSpeakerTemplateID, bParam, eColor, sMsg);
            return;
        }
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_SAY);
        getNpcScriptInfo().setNext(next);
        getNpcScriptInfo().setPrev(prev);
        getNpcScriptInfo().setDelay(delay);
        getNpcScriptInfo().setParam(bParam);
        getNpcScriptInfo().setSpeakerType(speakerTypeID);
        getNpcScriptInfo().setSpeakerTemplateID(speakerTemplateID);
        getNpcScriptInfo().setAnotherSpeakerTemplateID(anotherSpeakerTemplateID);
        getNpcScriptInfo().setOverrideSpeakerTemplateID(overrideSpeakerTemplateID);
        getNpcScriptInfo().setColor((byte) eColor);
        getNpcScriptInfo().setText(sMsg);
        write(new LP_SayScriptMessage(getNpcScriptInfo()));
    }

    public void askYesNo(String sMsg) {
        askYesNo(0, sMsg);
    }

    public void askYesNo(int bParam, String sMsg) {
        askYesNo(getTemplateID(), bParam, sMsg);
    }

    public void askYesNo(int nSpeakerTemplateID, int bParam, String sMsg) {
        askYesNo(nSpeakerTemplateID, nSpeakerTemplateID, bParam, sMsg);
    }

    public void askYesNo(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg) {
        askYesNo(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg);
    }

    public void askYesNo(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg) {
        askYesNo(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg);
    }

    public void askYesNo(int nSpeakerTypeID, int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, int eColor, String sMsg) {
        if (sMsg.contains("#L")) {
            askMenu(nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, sMsg);
            return;
        }
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_ASK_YES_NO);
        getClient().write(new LP_ScriptMessage(NpcMessageType.NM_ASK_YES_NO, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, null, null, null));
    }

    public void askMenu(String sMsg) {
        askMenu(0, sMsg);
    }

    public void askMenu(int bParam, String sMsg) {
        askMenu(getTemplateID(), bParam, sMsg);
    }

    public void askMenu(int nSpeakerTemplateID, int bParam, String sMsg) {
        askMenu(nSpeakerTemplateID, -1, bParam, sMsg);
    }

    public void askMenu(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg) {
        askMenu(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg);
    }

    public void askMenu(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg) {
        askMenu(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg);
    }

    public void askMenu(int nSpeakerTypeID, int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, int eColor, String sMsg) {
        if (!sMsg.contains("#L")) {
            say(nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, sMsg, false, false, 0);
            return;
        }
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_ASK_MENU);
        getClient().write(new LP_ScriptMessage(NpcMessageType.NM_ASK_MENU, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, null, null, null));
    }

    public void selfTalk(String text) {
        getClient().write(new LP_SelfTalkScriptMessage(text));
    }
}
