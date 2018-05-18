package com.msemu.world.client.scripting;

import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.wvscontext.LP_GuildResult;
import com.msemu.world.Channel;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.party.PartyMember;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.guild.operations.InputGuildName;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.FieldInstanceType;
import com.msemu.world.enums.ScriptType;
import com.msemu.world.enums.Stat;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by Weber on 2018/5/18.
 */
public abstract class AbstractInteractionManager {

    @Getter
    private int templateID;

    @Getter
    private Character character;

    public AbstractInteractionManager(Character character, int templateID) {
        this.character = character;
        this.templateID = templateID;
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

    public void addStr(int amount) {
        getCharacter().addStat(Stat.STR, amount);
    }

    public void addDex(int amount) {
        getCharacter().addStat(Stat.DEX, amount);
    }

    public void addInt(int amount) {
        getCharacter().addStat(Stat.INT, amount);
    }

    public void addLuk(int amount) {
        getCharacter().addStat(Stat.LUK, amount);
    }

    public void addHp(int amount) {
        getCharacter().addStat(Stat.HP, amount);
    }

    public void addMp(int amount) {
        getCharacter().addStat(Stat.MP, amount);
    }

    public void addMaxHp(int amount) {
        getCharacter().addStat(Stat.MAX_HP, amount);
    }

    public void addMaxMp(int amount) {
        getCharacter().addStat(Stat.MAX_MP, amount);
    }

    public void addExp(int amount) {
        getCharacter().addExp(amount);
    }

    public void gainMony(int amount) {
        getCharacter().addMoney(amount);
    }

    public void addAp(int amount) {
        getCharacter().addStat(Stat.AP, amount);
    }

    public void addSp(int jobLevel, int amount) {
        getCharacter().addSp(jobLevel, amount);
    }


    public long getMoney() {
        return getCharacter().getMoney();
    }


    public int getHp() {
        return getCharacter().getStat(Stat.HP);
    }

    public int getMaxHp() {
        return getCharacter().getCurrentMaxHp();
    }

    public int getMp() {
        return getCharacter().getStat(Stat.MP);
    }

    public int getMaxMp() {
        return getCharacter().getCurrentMaxMp();
    }

    public int getStr() {
        return getCharacter().getStat(Stat.STR);
    }

    public int getDex() {
        return getCharacter().getStat(Stat.DEX);
    }


    public int getInt() {
        return getCharacter().getStat(Stat.INT);
    }


    public int getLuk() {
        return getCharacter().getStat(Stat.LUK);
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
        if (portal != null) {
            getCharacter().warp(field, portal);
        } else {
            getCharacter().warp(field);
        }
    }

    public void dispose() {
        getCharacter().getScriptManager().stopScript();
    }

    public void write(OutPacket<GameClient> outPacket) {
        getClient().write(outPacket);
    }

}
