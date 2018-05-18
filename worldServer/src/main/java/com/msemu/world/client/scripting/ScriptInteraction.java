package com.msemu.world.client.scripting;

import com.msemu.commons.data.enums.QuestRequirementDataType;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.data.templates.quest.QuestInfo;
import com.msemu.commons.data.templates.quest.reqs.QuestNpcReqData;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.field.LP_FieldEffect;
import com.msemu.core.network.packets.out.field.LP_InGameCurNodeEventEnd;
import com.msemu.core.network.packets.out.npc.LP_NpcChangeController;
import com.msemu.core.network.packets.out.npc.LP_NpcLeaveField;
import com.msemu.core.network.packets.out.npc.LP_NpcSpecialAction;
import com.msemu.core.network.packets.out.npc.LP_NpcUpdateLimitedInfo;
import com.msemu.core.network.packets.out.script.LP_SayScriptMessage;
import com.msemu.core.network.packets.out.script.LP_ScriptMessage;
import com.msemu.core.network.packets.out.script.LP_SelfTalkScriptMessage;
import com.msemu.core.network.packets.out.user.local.*;
import com.msemu.core.network.packets.out.user.local.effect.LP_UserEffectLocal;
import com.msemu.core.network.packets.out.wvscontext.LP_FuncKeySetByScript;
import com.msemu.core.network.packets.out.wvscontext.LP_GuildResult;
import com.msemu.world.Channel;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.effect.*;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.client.character.quest.QuestManager;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.effect.MobHPTagFieldEffect;
import com.msemu.world.client.field.effect.ObjectFieldEffect;
import com.msemu.world.client.field.effect.ScreenDelayFieldEffect;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.Npc;
import com.msemu.world.client.guild.operations.InputGuildName;
import com.msemu.world.data.MobData;
import com.msemu.world.data.NpcData;
import com.msemu.world.data.QuestData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Weber on 2018/4/28.
 */
public class ScriptInteraction {

    @Getter
    public static final List<Npc> requestNpcs = new ArrayList<>();
    @Getter
    protected Character character;
    @Getter
    private ScriptType scriptType;
    @Getter
    private String scriptName;
    @Getter
    private int parentID;
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
        } else if (scriptType.equals(ScriptType.QUEST)) {
            QuestInfo qi = QuestData.getInstance().getQuestInfoById(parentID);
            if (scriptName.endsWith(ScriptManager.QUEST_COMPLETE_SCRIPT_END_TAG)) {
                Optional<QuestNpcReqData> rData = qi.getCompleteReqsData().stream()
                        .filter(req -> req.getType().equals(QuestRequirementDataType.npc))
                        .map(req -> (QuestNpcReqData) req)
                        .findFirst();
                if (rData.isPresent()) {
                    speakerTemplateID = rData.get().getNpcId();
                }
            } else if (scriptName.endsWith(ScriptManager.QUEST_START_SCRIPT_END_TAG)) {
                Optional<QuestNpcReqData> rData = qi.getStartReqsData().stream()
                        .filter(req -> req.getType().equals(QuestRequirementDataType.npc))
                        .map(req -> (QuestNpcReqData) req)
                        .findFirst();
                if (rData.isPresent()) {
                    speakerTemplateID = rData.get().getNpcId();
                }
            }
        }
    }

    public ScriptInteraction(Character character) {
        this.character = character;
    }

    public GameClient getClient() {
        return getCharacter().getClient();
    }

    public Channel getChannel() {
        return getClient().getChannelInstance();
    }

    public int getFieldID() {
        return getCharacter().getField().getId();
    }

    public void openNpc(int npc, String scriptName) {
        dispose();
        getCharacter().getScriptManager().startScript(npc, scriptName, ScriptType.NPC);
    }

    public void showGuildCreateWindow() {
        getCharacter().write(new LP_GuildResult(new InputGuildName()));
    }

    public Party getParty() {
        return getCharacter().getParty();
    }

    public void setPartyField() {
        getCharacter().setFieldInstanceType(FieldInstanceType.PARTY);
    }

    public void giveItem(int itemID) {
        giveItem(itemID, 1);
    }

    public void giveItem(int itemID, int quantity) {
        getCharacter().giveItem(itemID, quantity);
    }

    public void giveExp(int deltaExp) {
        getCharacter().addExp(deltaExp);
    }

    public void teachSkill(int skillID, int level) {
        Skill skill = SkillData.getInstance().getSkillById(skillID);
        skill.setCurrentLevel(level);
        getCharacter().teachSkill(skill);
    }

    public void teachSkill(int skillID, int level, int masterLevel) {
        Skill skill = SkillData.getInstance().getSkillById(skillID);
        skill.setCurrentLevel(level);
        skill.setMasterLevel(masterLevel);
        getCharacter().teachSkill(skill);
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

    public void warp(int fieldID) {
        warp(fieldID, 0);
    }

    public void warp(int fieldID, String portalName) {
        Field field = getChannel().getField(fieldID);
        Portal portal = field.getPortalByName(portalName);
        warp(field, portal);

    }

    public void warp(int fieldID, int portalID) {
        Field field = getChannel().getField(fieldID);
        Portal portal = field.getPortalByID(portalID);
        warp(field, portal);
    }

    public void warp(Field field, Portal portal) {
        dispose();
        if (portal != null)
            getCharacter().warp(field, portal);
        else
            getCharacter().warp(field);
    }

    public void resetField() {
        resetField(getFieldID());
    }

    public void resetField(int fieldID) {
        Field field = getChannel().getField(fieldID);
        field.reset();
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
        mob.setOldPosition(pos.deepCopy());
        mob.setPosition(pos.deepCopy());
        Field field = getCharacter().getField();
        if (mob.getField() == null) {
            mob.setField(field);
        }
        field.spawnMob(mob);
    }

    public void showMobHP(int templateID) {
        getCharacter().getField().getMobs().stream()
                .filter(m -> m.getTemplateId() == templateID)
                .findFirst()
                .ifPresent(mob -> getCharacter().getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(mob))));
    }

    public void showHP() {
        getCharacter().getField().getMobs().stream()
                .filter(m -> m.getHp() > 0)
                .findFirst()
                .ifPresent(mob -> getCharacter().getField().broadcastPacket(new LP_FieldEffect(new MobHPTagFieldEffect(mob))));
    }

    public void setFuncKeyByScript(int skillID, int keyIdx) {
        getClient().write(new LP_FuncKeySetByScript(skillID, keyIdx));
    }

    public void setDirectionMode(boolean enable) {
        getClient().write(new LP_SetDirectionMode(enable));
    }

    public void setInGameDirectionMode(boolean enable) {
        getClient().write(new LP_SetInGameDirectionMode(enable));
    }

    public void setStandAloneMode(boolean enable) {
        getClient().write(new LP_SetStandAloneMode(enable, enable));
    }

    public void showHireTutor(boolean show) {
        getClient().write(new LP_HireTutor(show));
    }

    public void showTutorMsg(boolean ui, int type, String message) {
        getClient().write(new LP_TutorMsg());
    }

    public void setEmotion(FaceEmotion emotion, int duration) {
        getClient().write(new LP_EmotionLocal(emotion, duration));
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
        say(getSpeakerTemplateID(), bParam, sMsg, prev, next);
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

    public void say(int nSpeakerTypeID, int nSpeakerTemplateID, int nAnotherSpeakerTemplateID, int nOtherSpeakerTemplateID, int bParam, int eColor, String sMsg, boolean prev, boolean next, int tWait) {
        if (sMsg.contains("#L")) {
            askMenu(nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, sMsg);
            return;
        }
        NpcMessageType type;
        if (prev && next) {
            type = NpcMessageType.NM_SAY;
        } else if (prev) {
            type = NpcMessageType.NM_SAY_PREV;
        } else if (next) {
            type = NpcMessageType.NM_SAY_NEXT;
        } else {
            type = NpcMessageType.NM_SAY_OK;
        }
        getNpcScriptInfo().setLastMessageType(type);
        write(new LP_SayScriptMessage(nSpeakerTemplateID, sMsg, bParam, prev, next));
        //getClient().write(new LP_ScriptMessage(type, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, new int[]{prev ? 1 : 0, next ? 1 : 0, tWait}, null, null));
    }

    public void askYesNo(String sMsg) {
        askYesNo(0, sMsg);
    }

    public void askYesNo(int bParam, String sMsg) {
        askYesNo(getSpeakerTemplateID(), bParam, sMsg);
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
        getClient().write(new LP_ScriptMessage(NpcMessageType.NM_ASK_MENU, nSpeakerTypeID, nSpeakerTemplateID, nAnotherSpeakerTemplateID, nOtherSpeakerTemplateID, bParam, eColor, new String[]{sMsg}, null, null, null));
    }

    public void selfTalk(String text) {
        getClient().write(new LP_SelfTalkScriptMessage(text));
    }

    /////////////////////////////////////////////////////////////////////////

    public void executeInGameDirectionEvent(int mod, String data, int[] values) {
        InGameDirectionEventOpcode type = InGameDirectionEventOpcode.getType(mod);
        getClient().write(new LP_InGameDirectionEvent(type, data, values));
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

    public void onNpcDirectionEffect(int npcID, String data, int value, Position pos) {
        onNpcDirectionEffect(npcID, data, value, pos.getX(), pos.getY());
    }

    public void onNpcDirectionEffect(int npcID, String data, int value, int x, int y) {
        final Npc npc = getRequestNpcs().stream()
                .filter(e -> e.getTemplateId() == npcID).findFirst().orElse(null);
        if (npc != null) {
            executeInGameDirectionEvent(InGameDirectionEventOpcode.InGameDirectionEvent_EffectPlay.getValue(), data, new int[]{value, x, y, 1, 1, 0, npc.getObjectId(), 0});
        }
    }
    ///////////////////////////////////////////////////////////////////////////

    public void playMovie(String data) {
        getClient().write(new LP_PlayMovieClip(data, true));
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_PLAY_MOVIE_CLIP);
    }

    public void playMovieURL(String url) {
        getClient().write(new LP_PlayMovieClipURL(url));
        getNpcScriptInfo().setLastMessageType(NpcMessageType.NM_PLAY_MOVIE_CLIP_URL);
    }

    //////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////FIELD///////////////////////////////////////
    public void setInGameCurNodeEventEnd(boolean enable) {
        getClient().write(new LP_InGameCurNodeEventEnd(enable));
    }


    public void onScreenDelayedFieldEffect(boolean broadcast, String effect, int delay) {
        write(broadcast, new LP_FieldEffect(new ScreenDelayFieldEffect(effect, delay)));
    }

    public void onScreenDelayedFieldEffect(String effect, int delay) {
        onScreenDelayedFieldEffect(false, effect, delay);
    }

    public void onObjectFieldEffect(boolean broadcast, String effect) {
        write(broadcast, new LP_FieldEffect(new ObjectFieldEffect(effect)));
    }

    public void onObjectFieldEffect(String effect) {
        onObjectFieldEffect(false, effect);
    }

    ///////////////////////////////////////////////////////////////////////////////////\


    public void spawnNPCRequestController(int npcid, int x, int y) {
        spawnNPCRequestController(npcid, x, y, 0);
    }

    public void spawnNPCRequestController(int npcID, int x, int y, int f) {
        Npc npc = getRequestNpcs().stream()
                .filter(e -> e.getTemplateId() == npcID).findFirst().orElse((createAndGetNpcRequestController(npcID)));
        npc.setPosition(new Position(x, y));
        npc.setCy(y);
        npc.setRx0(x - 50);
        npc.setRx1(x + 50);
        npc.setF(f);
        Foothold fh = getCharacter().getField().getFootholdTree().findBelow(new Position(x, y), false);
        npc.setFh(fh == null ? 0 : fh.getId());
        npc.setObjectId(getCharacter().getField().getNewObjectID());
        getClient().write(new LP_NpcChangeController(npc, true));
        getClient().write(new LP_NpcSpecialAction(npc, "summon", 0, false));
    }

    public Npc createAndGetNpcRequestController(int npcID) {
        Npc npc = NpcData.getInstance().getNpcFromTemplate(npcID);
        getRequestNpcs().add(npc);
        return npc;
    }

    public void removeNPCRequestController(int npcID) {
        final Npc oldNpc = getRequestNpcs().stream()
                .filter(e -> e.getTemplateId() == npcID).findFirst().orElse(null);
        if (oldNpc != null) {
            getRequestNpcs().remove(oldNpc);
            getClient().write(new LP_NpcChangeController(oldNpc, false));
            getClient().write(new LP_NpcLeaveField(oldNpc));
        }
    }

    public void updateNPCSpecialAction(int npcID, int value, int x, int y) {
        final Npc oldNpc = getRequestNpcs().stream()
                .filter(e -> e.getTemplateId() == npcID).findFirst().orElse(null);
        if (oldNpc != null) {
            getClient().write(new LP_NpcUpdateLimitedInfo(oldNpc, value, x, y));
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public void write(OutPacket<GameClient> packet) {
        write(false, packet);
    }

    public void write(boolean broadcast, OutPacket<GameClient> packet) {
        if (broadcast) {
            getCharacter().getField().broadcastPacket(packet);
        } else {
            getCharacter().write(packet);
        }
    }

    public void dispose() {
        getCharacter().getScriptManager().stopScript();
    }

    ////////////////////////////QUEST////////////////////////////////////

    public void startQuest() {
        if (getScriptType().equals(ScriptType.QUEST)) {
            final int questID = getParentID();
            startQuest(questID);
        }
    }

    public void startQuest(int questID) {
        QuestManager qm = getCharacter().getQuestManager();
        if (!qm.hasQuestInProgress(questID)) {
            qm.startQuest(questID);
        }
    }

    public boolean canStartQuest() {
        if (getScriptType().equals(ScriptType.QUEST)) {
            final int questID = getParentID();
            return canStartQuest(questID);
        }
        return false;
    }

    public boolean canStartQuest(int questID) {
        return getCharacter().getQuestManager().canStartQuest(questID);
    }

    public boolean hasQuestInProgress() {
        if (getScriptType().equals(ScriptType.QUEST)) {
            final int questID = getParentID();
            return hasQuestInProgress(questID);
        }
        return false;
    }

    public boolean hasQuestInProgress(int questID) {
        return getCharacter().getQuestManager().hasQuestInProgress(questID);
    }

    public boolean hasQuestCompleted() {
        if (getScriptType().equals(ScriptType.QUEST)) {
            final int questID = getParentID();
            return hasQuestCompleted(questID);
        }
        return false;
    }

    public boolean hasQuestCompleted(int questID) {
        QuestManager qm = getCharacter().getQuestManager();
        return qm.hasQuestCompleted(questID);
    }

    public void completeQuest() {
        if (getScriptType().equals(ScriptType.QUEST)) {
            final int questID = getParentID();
            completeQuest(questID);
        }
    }

    public void completeQuest(int questID) {
        QuestManager qm = getCharacter().getQuestManager();
        if (qm.hasQuestInProgress(questID)) {
            qm.completeQuest(questID);
        }
    }

    public String getQuestRecordEx(int questID) {
        QuestManager qm = getCharacter().getQuestManager();
        Quest quest = qm.getQuestsList().get(questID);
        return quest == null ? "" : quest.getQrExValue();
    }

    public void setQuestRecord(int questID, String qRValue) {
        QuestManager qm = getCharacter().getQuestManager();
        qm.setQuestRecord(questID, qRValue);
    }

    public void setQuestRecordEx(int questID, String qRExValue) {
        QuestManager qm = getCharacter().getQuestManager();
        qm.setQuestRecordEx(questID, qRExValue);
    }

    //////////////////////////////// user effect ///////////////////

    public void playPortalEffect() {
        write(new LP_UserEffectLocal(new PlayPortalSEUserEffect()));
    }

    public void showAvatarOrientedEffect(String effect) {
        write(new LP_UserEffectLocal(new AvatarOrientedUserEffect(effect)));
    }

    public void showReservedEffect(String effect) {
        showReservedEffect(false, 0, 0, effect);
    }

    public void showReservedEffect(boolean screenCoord, int rx, int ry, String effect) {
        write(new LP_UserEffectLocal(new ReservedUserEffect(screenCoord, rx, ry, effect)));
    }

    public void showExpItemConsumedEffect() {
        write(new LP_UserEffectLocal(new ExpItemConsumedUserEffect()));
    }

    public void playSoundWithMuteBGM(String name) {
        write(new LP_UserEffectLocal(new PlaySoundWithMuteBgmUserEffect(name)));
    }

    public void playExclSoundWithDownBGM(String name) {
        playExclSoundWithDownBGM(name, 0);
    }

    public void playExclSoundWithDownBGM(String name, int down) {
        write(new LP_UserEffectLocal(new PlayExclSoundWithDownBGMUserEffect(name, down)));
    }

    public void showSoulStoneUse(int itemID) {
        write(new LP_UserEffectLocal(new SoulStoneUseUserEffect(itemID)));
    }

    public void showPvpChampionEffect() {
        showPvpChampionEffect(30000);
    }

    public void showPvpChampionEffect(int duration) {
        write(new LP_UserEffectLocal(new PvPChampionUserEffect(duration)));
    }

    public void showFadeIntOutEffect(int timeFadeIn, int timeDelay, int timeFadeOut, int alpha) {
        write(new LP_UserEffectLocal(new FadeInOutUserEffect(timeFadeIn,
                timeDelay, timeFadeOut, alpha)));
    }

    public void showMobSkillHitEffect(int mobSkillID, int mobSkillSlv) {
        write(new LP_UserEffectLocal(new MobSkillHitUserEffect(mobSkillID, mobSkillSlv)));
    }

}
