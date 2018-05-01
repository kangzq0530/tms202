package com.msemu.world.client.scripting;

import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.field.FieldEffect;
import com.msemu.core.network.packets.out.script.ScriptMessage;
import com.msemu.core.network.packets.out.user.local.*;
import com.msemu.core.network.packets.out.wvscontext.FuncKeySetByScript;
import com.msemu.core.network.packets.out.wvscontext.GuildResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.effect.MobHPTagFieldEffect;
import com.msemu.world.client.guild.operations.InputGuildName;
import com.msemu.world.client.life.Mob;
import com.msemu.world.data.MobData;
import com.msemu.world.enums.*;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/28.
 */
public class ScriptInteraction {

    @Getter
    private ScriptType scriptType;
    @Getter
    private String scriptName;
    @Getter
    private int parentID;
    @Getter
    protected Character character;
    @Getter
    private int speakerTemplateID = 2007;
    @Getter
    private NpcScriptInfo npcScriptInfo;

    public ScriptInteraction(ScriptType scriptType, int parentID, String scriptName, Character character) {
        this.character = character;
        this.parentID = parentID;
        this.scriptName = scriptName;
        this.scriptType = scriptType;
        this.npcScriptInfo = new NpcScriptInfo();
        if (scriptType.equals(ScriptType.NPC)) {
            this.speakerTemplateID = parentID;
        }
    }

    public ScriptInteraction(Character character) {
        this.character = character;
    }

    public GameClient getClient() {
        return getCharacter().getClient();
    }

    public void showGuildCreateWindow() {
        getCharacter().write(new GuildResult(new InputGuildName()));
    }

    public Party getParty() {
        return getCharacter().getParty();
    }

    public void setPartyField() {
        getCharacter().setFieldInstanceType(FieldInstanceType.PARTY);
    }

    public boolean isPartyLeader() {
        return getCharacter().getParty() != null & getCharacter().getParty().getPartyLeaderID() == getCharacter().getId();
    }

    public void dropMessage(ChatMsgType msgType, String message) {
        getCharacter().dropMessage();
    }

    public void dropMessage(String message) {
        getCharacter().dropMessage();
    }

    public boolean checkParty(boolean isLeader, int membersCount, int minLevel, int maxLevel) {
        if (getCharacter().getParty() == null) {
            dropMessage("You are not in a party.");
            return false;
        } else if (isLeader && isPartyLeader()) {
            dropMessage("You are not the party leader.");
            return false;
        } else if (!checkPartyLevel(minLevel, maxLevel)) {
            dropMessage(String.format("Check the level of all member is in the range between %d ~ %d", minLevel, maxLevel));
            return false;
        }
        boolean res = true;
        Character leader = getCharacter().getParty().getPartyLeader().getCharacter();
        if (leader == null || !leader.isOnline()) {
            dropMessage("Your leader is currently offline.");
            return false;
        } else {
            int fieldID = leader.getFieldID();
            for (PartyMember pm : getCharacter().getParty().getPartyMembers()) {
                if (pm != null) {
                    res &= pm.getCharacter() != null && pm.isOnline() && pm.getFieldID() == fieldID;
                }
            }
        }

        if (!res) {
            dropMessage("Make sure that your whole party is online and in the same map as the party leader.");
        }
        return res;
    }

    public boolean checkPartyLevel(int minLevel, int maxLevel) {
        return Arrays.stream(getParty().getPartyMembers())
                .allMatch(member -> member.getCharacterLevel() >= minLevel &&
                        member.getCharacterLevel() <= maxLevel);
    }

    public void warpParty(int id) {
        warpParty(id, true);
    }

    public void warpPartyOut(int id) {
        warpParty(id, false);
    }


    public void warpParty(int fieldID, boolean in) {
        if (character.getParty() == null) {
            getCharacter().setFieldInstanceType(in ? FieldInstanceType.PARTY : FieldInstanceType.CHANNEL);
            Field field = getCharacter().getOrCreateFieldByCurrentInstanceType(fieldID);
            getCharacter().warp(field);
        } else {
            for (PartyMember pm : character.getParty().getPartyMembers()) {
                if (pm != null && pm.getCharacter() != null) {
                    Character partyChr = pm.getCharacter();
                    partyChr.setFieldInstanceType(in ? FieldInstanceType.PARTY : FieldInstanceType.CHANNEL);
                    Field field = partyChr.getOrCreateFieldByCurrentInstanceType(fieldID);
                    partyChr.warp(field);
                }
            }
        }
    }


    public void spawnMob(int id) {
        spawnMob(id, 0, 0, false);
    }

    public void spawnMob(int id, boolean respawnable) {
        spawnMob(id, 0, 0, respawnable);
    }

    public void spawnMobOnChar(int id) {
        spawnMob(id, getCharacter().getPosition().getX(), getCharacter().getPosition().getY(), false);
    }

    public void spawnMobOnChar(int id, boolean respawnable) {
        spawnMob(id, getCharacter().getPosition().getX(), getCharacter().getPosition().getY(), respawnable);
    }

    public void spawnMob(int id, int x, int y, boolean isRespawn) {
        Mob mob = MobData.getInstance().getMobFromTemplate(id);
        Position pos = new Position(x, y);
        mob.setPosition(pos.deepCopy());
        mob.setPrevPos(pos.deepCopy());
        mob.setPosition(pos.deepCopy());
        mob.setNotRespawnable(!isRespawn);
        Field field = getCharacter().getField();
        if (mob.getField() == null) {
            mob.setField(field);
        }
        field.spawnLife(mob, null);
    }

    public void showMobHP(int templateID) {
        getCharacter().getField().getMobs().stream()
                .filter(m -> m.getTemplateId() == templateID)
                .findFirst()
                .ifPresent(mob -> getCharacter().getField().broadcastPacket(new FieldEffect(new MobHPTagFieldEffect(mob))));
    }

    public void showHP() {
        getCharacter().getField().getMobs().stream()
                .filter(m -> m.getHp() > 0)
                .findFirst()
                .ifPresent(mob -> getCharacter().getField().broadcastPacket(new FieldEffect(new MobHPTagFieldEffect(mob))));
    }

    public void setFuncKeyByScript(int skillID, int keyIdx) {
        getClient().write(new FuncKeySetByScript(skillID, keyIdx));
    }

    public void setDirectionMode(boolean enable) {
        getClient().write(new SetDirectionMode(enable));
    }

    public void setInGameDirectionMode(boolean enable) {
        getClient().write(new SetInGameDirectionMode(enable));
    }

    public void setStandAloneMode(boolean enable) {
        getClient().write(new SetStandAloneMode(enable, enable));
    }

    public void showHireTutor(boolean show) {
        getClient().write(new HireTutor(show));
    }

    public void showTutorMsg(boolean ui, int type, String message) {
        getClient().write(new TutorMsg());
    }

    public void setEmotion(FaceEmotion emotion, int duration) {
        getClient().write(new EmotionLocal(emotion, duration));
    }

    public void setEmotion(int emotion, int duration) {
        setEmotion(FaceEmotion.getByValue(emotion), duration);
    }
    /////////////////////////////////////////////////////////
    ////////////////////////NPC//////////////////////////////

    /////////////////////////////////////////////////////////
    public void say(String sMsg) {
        say(sMsg, false, false);
    }

    public void say(String sMsg, boolean prev, boolean next) {
        say(0, sMsg, prev, next);
    }

    public void say(int bParam, String sMsg, boolean prev, boolean next) {
        say(getSpeakerTemplateID(), bParam, sMsg, prev, next);
    }

    public void say(int nSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(nSpeakerTemplateID, -1, bParam, sMsg, prev, next);
    }

    public void say(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(nSpeakerTemplateID, nAnotherSpeakerTemplateID, -1, bParam, sMsg, prev, next);
    }

    public void say(int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, String sMsg, boolean prev, boolean next) {
        say(4, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, 0, sMsg, prev, next, 0);
    }

    public void say(int nSpeakerTypeID, int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, int eColor, String sMsg, boolean prev, boolean next, int tWait) {
        if (sMsg.contains("#L")) {
            askMenu(nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, sMsg);
            return;
        }
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_SAY);
        getClient().write(new ScriptMessage(this, NpcMessageType.NM_SAY, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, new int[]{prev ? 1 : 0, next ? 1 : 0, tWait}, null, null));
    }

    public void askYesNo(String sMsg) {
        askYesNo(0, sMsg);
    }

    public void askYesNo(int bParam, String sMsg) {
        askYesNo(getSpeakerTemplateID(), bParam, sMsg);
    }

    public void askYesNo(int nSpeakerTemplateID, int bParam, String sMsg) {
        askYesNo(nSpeakerTemplateID, -1, bParam, sMsg);
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
        getClient().write(new ScriptMessage(this, NpcMessageType.NM_ASK_YES_NO, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, null, null, null));
    }

    public void askMenu(String sMsg) {
        askMenu(0, sMsg);
    }

    public void askMenu(int bParam, String sMsg) {
        askMenu(getSpeakerTemplateID(), bParam, sMsg);
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
        getClient().write(new ScriptMessage(this, NpcMessageType.NM_ASK_MENU, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, null, null, null));
    }

    /////////////////////////////////////////////////////////////////////////

    public void executeInGameDirectionEvent(int mod, String data, int[] values) {
        InGameDirectionEventOpcode type = InGameDirectionEventOpcode.getType(mod);
        getClient().write(new InGameDirectionEvent(type, data, values));
        if (getNpcScriptInfo().getLastMessageType() != null || type == null) {
            return;
        }
        switch (type) {
            case InGameDirectionEvent_Delay:
            case InGameDirectionEvent_ForcedInput:
            case InGameDirectionEvent_PatternInputRequest:
            case InGameDirectionEvent_CameraMove:
            case InGameDirectionEvent_CameraZoom:
                getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_ASK_GAME_DIRECTION_EVENT);
                break;
            case InGameDirectionEvent_Monologue:
                getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_MONOLOGUE);
                break;
        }
    }
    public void forcedAction(int[] values) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedAction.getValue(), null, values);
    }

    public void exceTime(int time) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_Delay.getValue(), null, new int[]{time});
    }

    public void getEventEffect(String data, int[] values) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_EffectPlay.getValue(), data, values);
    }

    public void playerWaite() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{0});
    }

    public void playerMoveLeft() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{1});
    }

    public void playerMoveRight() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{2});
    }

    public void playerJump() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{3});
    }

    public void playerMoveDown() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{4});
    }

    public void forcedInput(int input) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedInput.getValue(), null, new int[]{input});
    }

    public final void patternInput(String data, int[] values) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_PatternInputRequest.getValue(), data, values);
    }

    public final void cameraMove(int[] values) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_CameraMove.getValue(), null, values);
    }

    public final void cameraOnCharacter(int value) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_CameraOnCharacter.getValue(), null, new int[]{value});
    }

    public final void cameraZoom(int[] values) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_CameraZoom.getValue(), null, values);
    }

    public final void hidePlayer(boolean hide) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_VansheeMode.getValue(), null, new int[]{hide ? 1 : 0});
    }

    public final void faceOff(int value) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_FaceOff.getValue(), null, new int[]{value});
    }

    public void sendTellStory(String data, boolean lastLine) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_Monologue.getValue(), data, new int[]{lastLine ? 1 : 0});
    }

    public void removeAdditionalEffect() {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_RemoveAdditionalEffect.getValue(), null, null);
    }

    public void forcedMove(int value, int value1) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedMove.getValue(), null, new int[]{value, value1});
    }

    public void forcedFlip(int value) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_ForcedFlip.getValue(), null, new int[]{value});
    }

    public void inputUI(int value) {
        executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_InputUI.getValue(), null, new int[]{value});
    }
    ///////////////////////////////////////////////////////////////////////////

    public void playMovie(String data) {
        getClient().write(new PlayMovieClip(data, true));
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_PLAY_MOVIE_CLIP);
    }

    public void playMovieURL(String url) {
        getClient().write(new PlayMovieClipURL(url));
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_PLAY_MOVIE_CLIP_URL);
    }
}
